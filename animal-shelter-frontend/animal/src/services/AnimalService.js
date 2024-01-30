import { returnAuthToken } from "./AuthService";

export function getAnimals(pageNumber) {
    return fetch(`http://localhost:8081/api/animals?pageNumber=${pageNumber}`, {
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
        throw new Error('Error occured returning animals:' + response.status);
    })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}

export function adoptAnimal(animalId, userId) {
    return fetch(`http://localhost:8081/api/adoptions`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${returnAuthToken()}`
        },
        body: JSON.stringify({
            "userId": userId,
            "animalId": animalId
        })
    }).then((response) => {
        if (response.status === 201) {
            console.log(
                'Successfull fetch operation. Response status: ',
                response.status
            );
            return response.status;
        }
        throw new Error('Error occured during animal adoption:' + response.status);
    })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}

export function deleteAnimal(animalId) {
    return fetch(`http://localhost:8081/api/animals/${animalId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${returnAuthToken()}`
        }
    }).then((response) => {
        if (response.status === 204) {
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

export function getAnimalById(animalId) {
    return fetch(`http://localhost:8081/api/animals/${animalId}`, {
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
            return response.json();
        }
        throw new Error('Error occured returning users:' + response.status);
    })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}

export function addNewAnimal(animal) {
    return fetch("http://localhost:8081/api/animals", {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${returnAuthToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(animal)
    })
        .then((response) => {
            console.log(
                'Successfull fetch operation. Response status: ',
                response.status
            );
            return response;
        })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}

export function putAnimal(animal, id) {
    return fetch(`http://localhost:8081/api/animals/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${returnAuthToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(animal),
    })
        .then(async (response) => {
            if (response.ok) {
                console.log(
                    'Successfull fetch operation. Response status: ',
                    response.status
                );
                return response;
            }
            const error = await response.text();
            throw new Error(error);
        })
        .catch((error) => {
            console.log('Error occurred during fetch operation: ', error.message);
            throw new Error(error.message);
        });
}