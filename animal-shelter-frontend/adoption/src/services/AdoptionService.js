import { returnAuthToken } from "./AuthService";

export function getAdoptions(pageNumber) {
    return fetch(`http://localhost:8081/api/adoptions/all?pageNumber=${pageNumber}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${returnAuthToken()}`
        }
    }).then((response) => {
        if (response.ok) {
            console.log(
                'Successfull fetch operation. Response status: ',
                response.status
            );
            return response;
        }
        throw new Error('Error occured returning users:' + response.status);
    })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}

export function reactToAdoptionRequest(id, reaction) {
    return fetch(`http://localhost:8081/api/adoptions/${id}?reaction=${reaction}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${returnAuthToken()}`
        }
    }).then((response) => {
        if (response.ok) {
            console.log(
                'Successfull fetch operation. Response status: ',
                response.status
            );
            return response.ok;
        }
        throw new Error('Error occured returning users:' + response.status);
    })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}