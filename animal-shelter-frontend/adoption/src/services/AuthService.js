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