// ===== Member API 호출 모듈 =====

const MemberAPI = {

    async getAll(part, page, size) {
        const params = new URLSearchParams();
        if (part) params.append('part', part);
        if (page !== undefined) params.append('page', page);
        if (size !== undefined) params.append('size', size);

        const url = params.toString() ? `/members?${params.toString()}` : '/members';
        const res = await httpFetch(url);
        return res.json();
    },

    async getById(id) {
        const res = await httpFetch(`/members/${id}`);
        return res.json();
    },

    async createLion(data) {
        const res = await httpFetch('/members/lions', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async createStaff(data) {
        const res = await httpFetch('/members/staffs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async updateLion(id, data) {
        const res = await httpFetch(`/members/lions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async updateStaff(id, data) {
        const res = await httpFetch(`/members/staffs/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async delete(id) {
        await httpFetch(`/members/${id}`, { method: 'DELETE' });
    }
};

// ===== 페이징 상태 =====
let currentPage = 0;

function getMemberPageSize() {
    const el = document.getElementById('memberPageSize');
    return el ? parseInt(el.value) : 10;
}

// ===== Member UI 렌더링 =====

async function loadMembers() {
    const partFilter = document.getElementById('partFilter').value;
    const pageSize = getMemberPageSize();
    try {
        const result = await MemberAPI.getAll(partFilter || null, currentPage, pageSize);

        if (Array.isArray(result)) {
            renderMemberTable(result);
            renderPagination(null);
        } else {
            renderMemberTable(result.contents);
            renderPagination(result);
        }
    } catch (e) {
        renderMemberTable([]);
        renderPagination(null);
    }
}

function renderMemberTable(members) {
    const tbody = document.getElementById('memberTableBody');
    if (members.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-msg">등록된 멤버가 없습니다.</td></tr>';
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

// ===== 페이지네이션 렌더링 =====

function renderPagination(pageData) {
    const el = document.getElementById('memberPagination');
    if (!el) return;

    if (!pageData) {
        el.innerHTML = '';
        return;
    }

    el.innerHTML = `
        <button onclick="changeMemberPage(${pageData.number - 1})" ${pageData.number === 0 ? 'disabled' : ''}>이전</button>
        <span class="page-info">${pageData.number + 1} / ${pageData.totalPage} 페이지 (총 ${pageData.totalElement}명)</span>
        <button onclick="changeMemberPage(${pageData.number + 1})" ${pageData.last ? 'disabled' : ''}>다음</button>
    `;
}

function changeMemberPage(newPage) {
    if (newPage < 0) return;
    currentPage = newPage;
    loadMembers();
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
        currentPage = 0;
        await loadMembers();
    } catch (e) {}
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
        document.getElementById('editName').value = editingMember.name;
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
    } catch (e) {}
}

function closeEditMemberModal() {
    document.getElementById('editMemberModal').classList.remove('active');
    editingMember = null;
}

async function submitEditMember() {
    if (!editingMember) return;

    const data = {
        name: document.getElementById('editName').value.trim(),  // 추가
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
    } catch (e) {}
}

// ===== Member 삭제 =====

async function deleteMember(id, name) {
    if (!confirm(`"${name}" 멤버를 삭제하시겠습니까?`)) return;
    try {
        await MemberAPI.delete(id);
        await loadMembers();
    } catch (e) {}
}