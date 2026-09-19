// ===== Member API 호출 모듈 =====
// 모든 fetch 호출은 httpLog()를 통해 HTTP 통신 로그를 남긴다.

const MemberAPI = {

    // GET /members (파트 필터링 지원)
    async getAll(part) {
        const url = part ? `/members?part=${encodeURIComponent(part)}` : '/members';
        const res = await httpFetch(url);
        return res.json();
    },

    // GET /members/{id}
    async getById(id) {
        const res = await httpFetch(`/members/${id}`);
        return res.json();
    },

    // POST /members/lions
    async createLion(data) {
        const res = await httpFetch('/members/lions', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    // POST /members/staffs
    async createStaff(data) {
        const res = await httpFetch('/members/staffs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    // PUT /members/lions/{id}
    async updateLion(id, data) {
        const res = await httpFetch(`/members/lions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    // PUT /members/staffs/{id}
    async updateStaff(id, data) {
        const res = await httpFetch(`/members/staffs/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    // DELETE /members/{id}
    async delete(id) {
        await httpFetch(`/members/${id}`, { method: 'DELETE' });
    }
};

// ===== Member UI 렌더링 =====

function getMembersFromResponse(data) {
    if (Array.isArray(data)) {
        return data;
    }

    return data?.content || [];
}

async function loadMembers() {
    const partFilter = document.getElementById('partFilter').value;
    try {
        const data = await MemberAPI.getAll(partFilter || null);
        const members = getMembersFromResponse(data);
        renderMemberPageInfo(data);
        renderMemberTable(members);
    } catch (e) {
        renderMemberPageInfo(null);
        renderMemberTable([]);
    }
}

function renderMemberPageInfo(data) {
    const pageInfo = document.getElementById('memberPageInfo');
    if (!pageInfo) return;

    if (!data || Array.isArray(data)) {
        pageInfo.textContent = '';
        return;
    }

    const currentPage = Number.isInteger(data.page) ? data.page + 1 : '-';
    const totalPages = data.totalPages ?? '-';
    const totalElements = data.totalElements ?? 0;
    pageInfo.textContent = `전체 ${totalElements}명 | 현재 ${currentPage}페이지 / 전체 ${totalPages}페이지`;
}

function renderMemberTable(members) {
    const tbody = document.getElementById('memberTableBody');
    if (members.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-msg">등록된 멤버가 없습니다. 위 폼에서 추가해보세요!</td></tr>';
        return;
    }

    tbody.innerHTML = members.map(m => `
        <tr>
            <td>${m.id}</td>
            <td><strong>${m.name}</strong></td>
            <td>${m.major}</td>
            <td>${m.generation}기</td>
            <td>${m.part}</td>
            <td><span style="color:${m.roleName === '아기사자' ? '#ff7710' : '#3498db'}">${m.roleName}</span></td>
            <td>
                <div class="btn-group">
                    <button class="btn btn-info btn-sm" onclick="openEditMemberModal(${m.id})">수정</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteMember(${m.id}, '${m.name}')">삭제</button>
                    <button class="btn btn-secondary btn-sm" onclick="showAssignmentsForMember(${m.id}, '${m.name}')">과제</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// ===== Member 등록 =====

function showCreateForm() {
    const roleType = document.getElementById('createRoleType').value;
    const extraField = document.getElementById('createExtraField');
    if (roleType === 'LION') {
        extraField.innerHTML = '<input id="createStudentId" placeholder="학번">';
    } else {
        extraField.innerHTML = '<input id="createPosition" placeholder="직책 (대표, 부대표 등)">';
    }
}

async function createMember() {
    const roleType = document.getElementById('createRoleType').value;
    const name = document.getElementById('createName').value.trim();
    const major = document.getElementById('createMajor').value.trim();
    const generation = parseInt(document.getElementById('createGeneration').value);
    const part = document.getElementById('createPart').value;

    if (!name || !major || !generation || !part) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    try {
        if (roleType === 'LION') {
            const studentId = document.getElementById('createStudentId').value.trim();
            await MemberAPI.createLion({ name, major, generation, part, studentId });
        } else {
            const position = document.getElementById('createPosition').value.trim();
            await MemberAPI.createStaff({ name, major, generation, part, position });
        }
        clearCreateForm();
        await loadMembers();
    } catch (e) {
        // 에러는 httpFetch에서 이미 로그에 기록됨
    }
}

function clearCreateForm() {
    document.getElementById('createName').value = '';
    document.getElementById('createMajor').value = '';
    document.getElementById('createGeneration').value = '';
    showCreateForm();
}

// ===== Member 수정 (Modal) =====

let editingMember = null;

async function openEditMemberModal(id) {
    try {
        editingMember = await MemberAPI.getById(id);
        const modal = document.getElementById('editMemberModal');
        document.getElementById('editName').textContent = editingMember.name;
        document.getElementById('editMajor').value = editingMember.major;
        document.getElementById('editGeneration').value = editingMember.generation;
        document.getElementById('editPart').value = editingMember.part;

        const extraField = document.getElementById('editExtraField');
        if (editingMember.roleName === '아기사자') {
            extraField.innerHTML = `
                <label>학번</label>
                <input id="editStudentId" value="${editingMember.studentId || ''}">
            `;
        } else {
            extraField.innerHTML = `
                <label>직책</label>
                <input id="editPosition" value="${editingMember.position || ''}">
            `;
        }
        modal.classList.add('active');
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

function closeEditMemberModal() {
    document.getElementById('editMemberModal').classList.remove('active');
    editingMember = null;
}

async function submitEditMember() {
    if (!editingMember) return;

    const data = {
        major: document.getElementById('editMajor').value.trim(),
        generation: parseInt(document.getElementById('editGeneration').value),
        part: document.getElementById('editPart').value
    };

    try {
        if (editingMember.roleName === '아기사자') {
            data.studentId = document.getElementById('editStudentId').value.trim();
            await MemberAPI.updateLion(editingMember.id, data);
        } else {
            data.position = document.getElementById('editPosition').value.trim();
            await MemberAPI.updateStaff(editingMember.id, data);
        }
        closeEditMemberModal();
        await loadMembers();
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

// ===== Member 삭제 =====

async function deleteMember(id, name) {
    if (!confirm(`"${name}" 멤버를 삭제하시겠습니까?`)) return;
    try {
        await MemberAPI.delete(id);
        await loadMembers();
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}
