// ===== Assignment API 호출 모듈 =====

const AssignmentAPI = {

    // POST /assignments
    async create(memberId, data) {
        const res = await httpFetch('/assignments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ memberId: Number(memberId), ...data })
        });
        return res.json();
    },

    // GET /assignments
    async getAll() {
        const res = await httpFetch('/assignments');
        return res.json();
    },

    // GET /members/{memberId}/assignments
    async getByMember(memberId) {
        const res = await httpFetch(`/members/${memberId}/assignments`);
        return res.json();
    },

    // GET /assignments/{id}
    async getById(id) {
        const res = await httpFetch(`/assignments/${id}`);
        return res.json();
    },

    // GET /assignments/search?keyword=
    async search(keyword) {
        const res = await httpFetch(`/assignments/search?keyword=${encodeURIComponent(keyword)}`);
        return res.json();
    },

    // PUT /assignments/{id}
    async update(id, data) {
        const res = await httpFetch(`/assignments/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    // DELETE /assignments/{id}
    async delete(id) {
        await httpFetch(`/assignments/${id}`, {
            method: 'DELETE'
        });
    }
};

function getAssignmentsFromResponse(data) {
    return Array.isArray(data) ? data : (data?.content || []);
}

// ===== 공통: 멤버 드롭다운 로드 =====

async function loadMemberSelect() {
    try {
        const data = await MemberAPI.getAll();
        const members = getMembersFromResponse(data);
        const options = members.map(m =>
            `<option value="${m.id}">${m.name} (${m.roleName})</option>`
        ).join('');

        document.getElementById('createAssignmentMemberSelect').innerHTML =
            '<option value="">멤버 선택</option>' + options;

        document.getElementById('memberAssignmentSelect').innerHTML =
            '<option value="">멤버 선택</option>' + options;
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

// ===== 공통: 과제 목록 렌더링 =====

function renderAssignments(container, assignments) {
    if (assignments.length === 0) {
        container.innerHTML = '<div class="empty-msg">결과가 없습니다.</div>';
        return;
    }

    container.innerHTML = assignments.map(a => `
        <div class="assignment-list-item">
            <div class="info">
                <div class="title">${a.title}</div>
                <div class="desc">${a.description || '-'}</div>
                <div class="meta">ID: ${a.id} | 작성자: ${a.memberName}</div>
            </div>
        </div>
    `).join('');
}

// ===== 공통: 단건 렌더링 =====

function renderSingleAssignment(container, a) {
    container.innerHTML = `
        <div class="assignment-list-item">
            <div class="info">
                <div class="title">${a.title}</div>
                <div class="desc">${a.description || '-'}</div>
                <div class="meta">ID: ${a.id} | 멤버 ID: ${a.memberId} | 작성자: ${a.memberName}</div>
            </div>
        </div>
    `;
}

// ===== 1. 과제 등록 =====

async function createAssignment() {
    const memberId = document.getElementById('createAssignmentMemberSelect').value;
    if (!memberId) {
        alert('멤버를 먼저 선택해주세요.');
        return;
    }

    const title = document.getElementById('assignmentTitle').value.trim();
    const description = document.getElementById('assignmentDesc').value.trim();

    if (!title) {
        alert('제목을 입력해주세요.');
        return;
    }

    try {
        await AssignmentAPI.create(memberId, { title, description });
        document.getElementById('assignmentTitle').value = '';
        document.getElementById('assignmentDesc').value = '';
        alert('과제가 등록되었습니다.');
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

// ===== 2. 전체 과제 조회 =====

async function loadAllAssignments() {
    const container = document.getElementById('allAssignmentList');
    try {
        const assignments = getAssignmentsFromResponse(await AssignmentAPI.getAll());
        renderAssignments(container, assignments);
    } catch (e) {
        container.innerHTML = '<div class="empty-msg">조회 실패</div>';
    }
}

// ===== 3. 멤버별 과제 조회 =====

async function loadMemberAssignments() {
    const memberId = document.getElementById('memberAssignmentSelect').value;
    const container = document.getElementById('memberAssignmentList');

    if (!memberId) {
        alert('멤버를 선택해주세요.');
        return;
    }

    try {
        const assignments = getAssignmentsFromResponse(await AssignmentAPI.getByMember(memberId));
        renderAssignments(container, assignments);
    } catch (e) {
        container.innerHTML = '<div class="empty-msg">조회 실패</div>';
    }
}

// ===== 4. 단건 조회 =====

async function loadAssignmentById() {
    const id = document.getElementById('assignmentIdInput').value;
    const container = document.getElementById('singleAssignmentResult');

    if (!id) {
        alert('과제 ID를 입력해주세요.');
        return;
    }

    try {
        const assignment = await AssignmentAPI.getById(id);
        renderSingleAssignment(container, assignment);
    } catch (e) {
        container.innerHTML = '<div class="empty-msg">조회 실패</div>';
    }
}

// ===== 5. 제목 검색 =====

async function searchAssignments() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    const container = document.getElementById('searchAssignmentList');

    if (!keyword) {
        alert('검색어를 입력해주세요.');
        return;
    }

    try {
        const results = getAssignmentsFromResponse(await AssignmentAPI.search(keyword));
        renderAssignments(container, results);
    } catch (e) {
        container.innerHTML = '<div class="empty-msg">검색 실패</div>';
    }
}

// ===== 6. 과제 수정 =====

let editingAssignmentId = null;

async function loadAssignmentForEdit() {
    const id = document.getElementById('editAssignmentIdInput').value;
    if (!id) {
        alert('과제 ID를 입력해주세요.');
        return;
    }

    try {
        const assignment = await AssignmentAPI.getById(id);
        editingAssignmentId = assignment.id;
        document.getElementById('editAssignmentTitleInput').value = assignment.title;
        document.getElementById('editAssignmentDescInput').value = assignment.description || '';
        document.getElementById('editAssignmentForm').style.display = 'block';
    } catch (e) {
        document.getElementById('editAssignmentForm').style.display = 'none';
    }
}

async function submitUpdateAssignment() {
    if (!editingAssignmentId) return;

    const data = {
        title: document.getElementById('editAssignmentTitleInput').value.trim(),
        description: document.getElementById('editAssignmentDescInput').value.trim()
    };

    if (!data.title) {
        alert('제목을 입력해주세요.');
        return;
    }

    try {
        await AssignmentAPI.update(editingAssignmentId, data);
        alert('과제가 수정되었습니다.');
        document.getElementById('editAssignmentForm').style.display = 'none';
        editingAssignmentId = null;
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

// ===== 7. 과제 삭제 =====

async function deleteAssignmentById() {
    const id = document.getElementById('deleteAssignmentIdInput').value;
    if (!id) {
        alert('과제 ID를 입력해주세요.');
        return;
    }

    if (!confirm(`과제 ID ${id}를 삭제하시겠습니까?`)) return;

    try {
        await AssignmentAPI.delete(id);
        alert('과제가 삭제되었습니다.');
        document.getElementById('deleteAssignmentIdInput').value = '';
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

// ===== 멤버 탭에서 "과제" 버튼 클릭 시 =====

function showAssignmentsForMember(memberId, memberName) {
    switchTab('assignment');
    document.getElementById('memberAssignmentSelect').value = memberId;
    loadMemberAssignments();
}
