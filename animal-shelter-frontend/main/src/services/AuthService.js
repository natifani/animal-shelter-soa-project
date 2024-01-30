export function login(credentials) {
    return fetch('http://localhost:8081/api/authenticate/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    }).then(async (response) => {
        if (response.status === 401 || response.status == 404) {
            throw new Error('Username or password incorrect.');
        }
        const data = await response.json();
        if (response.ok && data.token) {
            localStorage.setItem('user', JSON.stringify(data));
            return;
        }
        throw new Error('Could not login');
    }).catch((error) => {
        console.log('Error occurred during login: ', error.message);
        throw new Error(error.message);
    });
}

export function logout() {
    const refreshToken = getCurrentUser().refreshToken;
    localStorage.removeItem('user');
    return fetch('http://localhost:8081/api/authenticate/logout', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'refreshToken': refreshToken
        })
    }).then((response) => {
        if (response.status !== 204) {
            throw new Error("Error during logout");
        }
    }).catch((error) => {
        console.log(error.message);
    });
}

export function register(credentials) {
    return fetch('http://localhost:8081/api/authenticate/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    }).then((response) => {
        if (response.ok) {
            console.log('User successfully registered');
            return response;
        }
        throw new Error('Error occured during user registration: ' + response.status);
    }).catch((error) => {
        console.log(error.message);
        throw new Error(error.message);
    });
}

export function getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
}

export function returnAuthToken() {
    const user = getCurrentUser();
    if (user && user.token) {
        return user.token;
    } else {
        return {};
    }
}

export function refreshToken() {
    const refreshToken = getCurrentUser().refreshToken;
    localStorage.removeItem('user');
    return fetch('http://localhost:8081/api/authenticate/refreshtoken', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'refreshToken': refreshToken
        })
    }).then(async (response) => {
        if (response.status !== 200) {
            throw new Error("Error during token refresh");
        } else {
            const data = await response.json();
            const user = getCurrentUser();
            user.token = data.accessToken;
            user.refreshToken = data.refreshToken;
            localStorage.setItem('user', user);
        }
    }).catch((error) => {
        console.log(error.message);
    });
}

