// ===== Assignment API 호출 모듈 =====

const AssignmentAPI = {

    async create(memberId, data) {
        const res = await httpFetch(`/members/${memberId}/assignments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async getAll(page, size) {
        const params = new URLSearchParams();
        if (page !== undefined) params.append('page', page);
        if (size !== undefined) params.append('size', size);

        const url = params.toString() ? `/assignments?${params.toString()}` : '/assignments';
        const res = await httpFetch(url);
        return res.json();
    },

    async getByMember(memberId) {
        const res = await httpFetch(`/members/${memberId}/assignments`);
        return res.json();
    },

    async getById(id) {
        const res = await httpFetch(`/assignments/${id}`);
        return res.json();
    },

    async search(keyword) {
        const res = await httpFetch(`/assignments/search?keyword=${encodeURIComponent(keyword)}`);
        return res.json();
    },

    async update(id, data) {
        const res = await httpFetch(`/assignments/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async delete(id) {
        await httpFetch(`/assignments/${id}`, { method: 'DELETE' });
    }
};

// ===== Comment API 호출 모듈 =====

const CommentAPI = {

    // GET /assignments/{assignmentId}/comments
    async getAll(assignmentId) {
        const res = await httpFetch(`/assignments/${assignmentId}/comments`);
        return res.json();
    },

    // POST /assignments/{assignmentId}/comments
    async create(assignmentId, content) {
        const res = await httpFetch(`/assignments/${assignmentId}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content })
        });
        return res.json();
    },

    // DELETE /comments/{id}
    async delete(id) {
        await httpFetch(`/comments/${id}`, { method: 'DELETE' });
    }
};

// ===== 과제 전체 조회 페이징 상태 =====
let assignmentCurrentPage = 0;

function getAssignmentPageSize() {
    const el = document.getElementById('assignmentPageSize');
    return el ? parseInt(el.value) : 10;
}

// ===== 공통: 멤버 드롭다운 로드 =====
async function loadMemberSelect() {
    try {
        const result = await MemberAPI.getAll(null, 0, 1000);
        const members = Array.isArray(result) ? result : result.contents;

        const options = members.map(m =>
            `<option value="${m.id}">${m.name} (${m.roleName})</option>`
        ).join('');

        document.getElementById('createAssignmentMemberSelect').innerHTML =
            '<option value="">멤버 선택</option>' + options;

        document.getElementById('memberAssignmentSelect').innerHTML =
            '<option value="">멤버 선택</option>' + options;
    } catch (e) {}
}

// ===== 공통: 과제 목록 렌더링 (댓글 버튼 포함) =====

function renderAssignments(container, assignments) {
    if (assignments.length === 0) {
        container.innerHTML = '<div class="empty-msg">결과가 없습니다.</div>';
        return;
    }

    container.innerHTML = assignments.map(a => `
        <div class="assignment-list-item" id="assignment-item-${a.id}">
            <div class="info">
                <div class="title">${a.title}</div>
                <div class="desc">${a.description || '-'}</div>
                <div class="meta">ID: ${a.id} | 작성자: ${a.memberName}</div>
            </div>
            <div style="margin-top: 8px;">
                <button class="btn btn-secondary btn-sm" onclick="toggleComments(${a.id})">
                    💬 댓글 보기
                </button>
            </div>
            <!-- 댓글 영역 (초기엔 숨김) -->
            <div id="comment-area-${a.id}" style="display:none; margin-top:10px; padding:10px; background:#f8f9fa; border-radius:8px;">
                <div id="comment-list-${a.id}" class="comment-list">
                    <div class="empty-msg">로딩 중...</div>
                </div>
                <!-- 댓글 작성 폼 (로그인 시에만 표시) -->
                <div id="comment-form-${a.id}" style="display:none; margin-top:10px;">
                    <div style="display:flex; gap:8px;">
                        <input id="comment-input-${a.id}" 
                               placeholder="댓글을 입력하세요..." 
                               style="flex:1; padding:6px 10px; border:1px solid #ddd; border-radius:6px; font-size:13px;"
                               onkeydown="if(event.key==='Enter') submitComment(${a.id})">
                        <button class="btn btn-primary btn-sm" onclick="submitComment(${a.id})">등록</button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

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

// ===== 댓글 토글 (열기/닫기) =====

async function toggleComments(assignmentId) {
    const area = document.getElementById(`comment-area-${assignmentId}`);
    const isHidden = area.style.display === 'none';

    if (isHidden) {
        area.style.display = 'block';
        // 로그인 상태면 댓글 작성 폼 표시
        if (localStorage.getItem('jwt_token')) {
            document.getElementById(`comment-form-${assignmentId}`).style.display = 'block';
        }
        await loadComments(assignmentId);
    } else {
        area.style.display = 'none';
    }
}

// ===== 댓글 목록 로드 =====

async function loadComments(assignmentId) {
    const listEl = document.getElementById(`comment-list-${assignmentId}`);
    try {
        const comments = await CommentAPI.getAll(assignmentId);
        renderComments(listEl, comments, assignmentId);
    } catch (e) {
        listEl.innerHTML = '<div class="empty-msg">댓글 로드 실패</div>';
    }
}

// ===== 댓글 목록 렌더링 =====

function renderComments(container, comments, assignmentId) {
    if (comments.length === 0) {
        container.innerHTML = '<div class="empty-msg" style="font-size:13px;">아직 댓글이 없습니다.</div>';
        return;
    }

    container.innerHTML = comments.map(c => `
        <div class="comment-item" style="padding:8px 0; border-bottom:1px solid #eee; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <span style="font-weight:bold; font-size:13px; color:#333;">${c.memberName}</span>
                <span style="font-size:12px; color:#999; margin-left:8px;">${formatDate(c.createdAt)}</span>
                <div style="font-size:14px; margin-top:4px;">${c.content}</div>
            </div>
            ${localStorage.getItem('jwt_token') ? `
                <button class="btn btn-danger btn-sm" 
                        style="font-size:11px; padding:2px 8px;"
                        onclick="deleteComment(${c.id}, ${assignmentId})">삭제</button>
            ` : ''}
        </div>
    `).join('');
}

// ===== 댓글 등록 =====

async function submitComment(assignmentId) {
    const input = document.getElementById(`comment-input-${assignmentId}`);
    const content = input.value.trim();

    if (!content) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    try {
        await CommentAPI.create(assignmentId, content);
        input.value = '';
        await loadComments(assignmentId); // 등록 후 목록 새로고침
    } catch (e) {}
}

// ===== 댓글 삭제 =====

async function deleteComment(commentId, assignmentId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) return;
    try {
        await CommentAPI.delete(commentId);
        await loadComments(assignmentId); // 삭제 후 목록 새로고침
    } catch (e) {}
}

// ===== 날짜 포맷 =====

function formatDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
}

// ===== 과제 전체 조회 페이지네이션 렌더링 =====

function renderAssignmentPagination(pageData) {
    const el = document.getElementById('assignmentPagination');
    if (!el) return;

    if (!pageData) {
        el.innerHTML = '';
        return;
    }

    el.innerHTML = `
        <button onclick="changeAssignmentPage(${pageData.number - 1})" ${pageData.number === 0 ? 'disabled' : ''}>이전</button>
        <span class="page-info">${pageData.number + 1} / ${pageData.totalPage} 페이지 (총 ${pageData.totalElement}건)</span>
        <button onclick="changeAssignmentPage(${pageData.number + 1})" ${pageData.last ? 'disabled' : ''}>다음</button>
    `;
}

function changeAssignmentPage(newPage) {
    if (newPage < 0) return;
    assignmentCurrentPage = newPage;
    loadAllAssignments();
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
    } catch (e) {}
}

// ===== 2. 전체 과제 조회 =====

async function loadAllAssignments() {
    const container = document.getElementById('allAssignmentList');
    const pageSize = getAssignmentPageSize();
    try {
        const result = await AssignmentAPI.getAll(assignmentCurrentPage, pageSize);
        renderAssignments(container, result.contents);
        renderAssignmentPagination(result);
    } catch (e) {
        container.innerHTML = '<div class="empty-msg">조회 실패</div>';
        renderAssignmentPagination(null);
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
        const assignments = await AssignmentAPI.getByMember(memberId);
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
        const results = await AssignmentAPI.search(keyword);
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
    } catch (e) {}
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
    } catch (e) {}
}

// ===== 멤버 탭에서 "과제" 버튼 클릭 시 =====

function showAssignmentsForMember(memberId, memberName) {
    switchTab('assignment');
    document.getElementById('memberAssignmentSelect').value = memberId;
    loadMemberAssignments();
}