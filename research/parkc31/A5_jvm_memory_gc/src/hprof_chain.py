"""힙덤프(.hprof)에서 LeakDemo.LEAK → ArrayList → Object[] → byte[] 연결 고리와 크기를 확인하는 간이 분석기 (MAT 대체 확인용)"""
import struct,sys,collections
f=open(sys.argv[1],'rb').read()
p=f.index(b'\0')+1; idsz=struct.unpack('>I',f[p:p+4])[0]; p+=12
rid=lambda o:int.from_bytes(f[o:o+idsz],'big')
T={2:1,4:1,5:2,6:4,7:8,8:1,9:2,10:4,11:8}; T[2]=idsz
strings={}; cname={}; cls={}; inst={}; objarr={}; prim=collections.Counter(); primsz=collections.Counter(); primlen={}
while p<len(f):
    tag=f[p]; ln=struct.unpack('>I',f[p+5:p+9])[0]; b=p+9; e=b+ln
    if tag==1: strings[rid(b)]=f[b+idsz:e].decode('utf8','replace')
    elif tag==2: cname[rid(b+4)]=strings.get(rid(b+8+idsz),'?')
    elif tag in(0x0c,0x1c):
        q=b
        while q<e:
            st=f[q]; q+=1
            if st==0xff: q+=idsz
            elif st in(1,): q+=2*idsz
            elif st in(2,3): q+=idsz+8
            elif st==4: q+=idsz+4
            elif st in(5,7): q+=idsz
            elif st==6: q+=idsz+4
            elif st==8: q+=idsz+8
            elif st==0x20:
                cid=rid(q); q+=idsz; q+=4; sup=rid(q); q+=idsz*6; q+=4
                n=struct.unpack('>H',f[q:q+2])[0]; q+=2
                for _ in range(n): t=f[q+2]; q+=3+T[t]
                n=struct.unpack('>H',f[q:q+2])[0]; q+=2; stat={}
                for _ in range(n):
                    nm=rid(q); t=f[q+idsz]; q+=idsz+1; stat[strings.get(nm)]=(t,rid(q) if t==2 else None); q+=T[t]
                n=struct.unpack('>H',f[q:q+2])[0]; q+=2; flds=[]
                for _ in range(n): flds.append((strings.get(rid(q)),f[q+idsz])); q+=idsz+1
                cls[cid]=(sup,stat,flds)
            elif st==0x21:
                oid=rid(q); cid=rid(q+4+idsz); n=struct.unpack('>I',f[q+4+2*idsz:q+8+2*idsz])[0]; d=q+8+2*idsz
                inst[oid]=(cid,d,n); q=d+n
            elif st==0x22:
                oid=rid(q); n=struct.unpack('>I',f[q+4+idsz:q+8+idsz])[0]; d=q+8+2*idsz
                objarr[oid]=[rid(d+i*idsz) for i in range(n)]; q=d+n*idsz
            elif st==0x23:
                oid=rid(q); n=struct.unpack('>I',f[q+4+idsz:q+8+idsz])[0]; t=f[q+8+idsz]
                prim[t]+=1; primsz[t]+=n*T[t]; primlen[oid]=(t,n); q+=9+idsz+n*T[t]
            else: raise SystemExit('unknown subtag %x'%st)
    p=e
def fields(oid):
    cid,d,n=inst[oid]; out={}; q=d
    while cid in cls:
        sup,stat,flds=cls[cid]
        for nm,t in flds:
            out.setdefault(nm, rid(q) if t==2 else int.from_bytes(f[q:q+T[t]],'big',signed=True)); q+=T[t]
        cid=sup
    return out
leak_cls=[c for c,nm in cname.items() if nm=='LeakDemo'][0]
lst=cls[leak_cls][1]['LEAK'][1]
fl=fields(lst); arr=objarr[fl['elementData']]
elems=[x for x in arr if x in primlen]
total=sum(primlen[x][1] for x in elems)
print("byte[] 전체: %d개, %.1fMB"%(prim[8],primsz[8]/1048576))
print("GC Root: static 필드 LeakDemo.LEAK")
print("  → %s (size=%d)"%(cname[inst[lst][0]],fl['size']))
print("  → Object[] elementData (length=%d)"%len(arr))
print("  → byte[] %d개, 개당 %dB, 합계 %.1fMB"%(len(elems),primlen[elems[0]][1],total/1048576))
print("힙 전체 byte[] 중 이 경로가 차지하는 비율: %.1f%%"%(100*total/primsz[8]))
