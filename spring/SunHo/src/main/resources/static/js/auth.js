// ===== Auth API 호출 및 로그인 상태 관리 모듈 =====

const TOKEN_KEY = 'accessToken';

const AuthAPI = {
    // POST /auth/login
    async login(name, password) {
        const res = await httpFetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, password })
        });
        return res.json();
    }
};

function saveToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function clearToken() {
    localStorage.removeItem(TOKEN_KEY);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

// base64url 문자열을 UTF-8 텍스트로 디코딩 (atob만 쓰면 한글 등 멀티바이트 문자가 깨짐)
function decodeJwtPayload(base64url) {
    const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    const binary = atob(base64);
    const bytes = Uint8Array.from(binary, c => c.charCodeAt(0));
    return new TextDecoder('utf-8').decode(bytes);
}

// JWT payload(sub, name, role, exp)를 디코딩. 없거나 만료됐으면 null.
function getCurrentUser() {
    const token = getToken();
    if (!token) return null;

    try {
        const payload = JSON.parse(decodeJwtPayload(token.split('.')[1]));
        if (payload.exp && Date.now() >= payload.exp * 1000) {
            clearToken();
            return null;
        }
        return { id: payload.sub, name: payload.name, role: payload.role };
    } catch (e) {
        clearToken();
        return null;
    }
}

async function login() {
    const name = document.getElementById('loginName').value.trim();
    const password = document.getElementById('loginPassword').value;

    if (!name || !password) {
        alert('이름과 비밀번호를 입력해주세요.');
        return;
    }

    try {
        const { accessToken } = await AuthAPI.login(name, password);
        saveToken(accessToken);
        document.getElementById('loginName').value = '';
        document.getElementById('loginPassword').value = '';
        updateAuthUI();
    } catch (e) {
        // 에러 로그는 httpFetch에서 처리
    }
}

function logout() {
    clearToken();
    updateAuthUI();
}

function updateAuthUI() {
    const user = getCurrentUser();
    const loginForm = document.getElementById('loginForm');
    const loginStatus = document.getElementById('loginStatus');

    if (user) {
        loginForm.style.display = 'none';
        loginStatus.style.display = 'flex';
        document.getElementById('loginStatusName').textContent = `${user.name}님 로그인됨`;
    } else {
        loginForm.style.display = 'flex';
        loginStatus.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', updateAuthUI);