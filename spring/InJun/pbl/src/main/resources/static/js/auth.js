function refreshAuthState() {
    const status = document.getElementById('authStatus');
    if (!status) return;

    const token = localStorage.getItem('accessToken');
    status.textContent = token && token.trim() ? '로그인됨 (JWT 저장됨)' : '로그아웃됨';
}

function showSignupExtraField() {
    const roleName = document.getElementById('signupRoleName').value;
    const container = document.getElementById('signupExtraField');
    container.innerHTML = roleName === 'LION'
        ? '<input id="signupStudentId" placeholder="학번">'
        : '<input id="signupPosition" placeholder="직책">';
}

async function signup() {
    const roleName = document.getElementById('signupRoleName').value;
    const name = document.getElementById('signupName').value.trim();
    const password = document.getElementById('signupPassword').value;
    const request = {
        name,
        major: document.getElementById('signupMajor').value.trim(),
        generation: Number(document.getElementById('signupGeneration').value),
        part: document.getElementById('signupPart').value,
        roleName,
        studentId: roleName === 'LION'
            ? document.getElementById('signupStudentId').value.trim()
            : null,
        position: roleName === 'STAFF'
            ? document.getElementById('signupPosition').value.trim()
            : null,
        password
    };

    const extraValue = roleName === 'LION' ? request.studentId : request.position;
    if (!request.name || !request.major || !request.generation || !request.part || !extraValue || !request.password) {
        showToast('회원가입 항목을 모두 입력해주세요.', 'error');
        return;
    }

    try {
        await httpFetch('/auth/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request)
        });

        document.getElementById('loginName').value = name;
        document.getElementById('loginPassword').value = password;
        await login(name, password, true);
    } catch (error) {
        // httpFetch가 서버의 회원가입 실패 사유를 표시합니다.
    }
}

async function login(nameOverride, passwordOverride, fromSignup = false) {
    const name = nameOverride ?? document.getElementById('loginName').value.trim();
    const password = passwordOverride ?? document.getElementById('loginPassword').value;

    if (!name || !password) {
        showToast('이름과 비밀번호를 입력해주세요.', 'error');
        return;
    }

    try {
        const response = await httpFetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, password })
        });
        const data = await response.json();

        if (!data.accessToken) {
            throw new Error('로그인 응답에 accessToken이 없습니다.');
        }

        localStorage.setItem('accessToken', data.accessToken);
        refreshAuthState();
        showToast(fromSignup ? '회원가입 후 바로 로그인되었습니다.' : '로그인되었습니다.', 'info');
        return true;
    } catch (error) {
        localStorage.removeItem('accessToken');
        refreshAuthState();
        showToast(
            fromSignup
                ? '회원가입은 완료됐지만 자동 로그인에 실패했습니다. 입력한 정보로 다시 로그인해주세요.'
                : '로그인에 실패했습니다. /auth/signup으로 회원가입을 먼저 했는지 확인해주세요.',
            'error'
        );
        return false;
    }
}

function logout() {
    localStorage.removeItem('accessToken');
    refreshAuthState();
    showToast('로그아웃되었습니다.', 'info');
}
