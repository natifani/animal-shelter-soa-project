import React, { useEffect, useState } from 'react';
import { Container } from 'react-bootstrap';
import { getAnimals } from '../services/AnimalService';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import { adoptAnimal } from '../services/AnimalService';
import { deleteAnimal } from '../services/AnimalService';
import { getCurrentUser, refreshToken } from '../services/AuthService';
import { jwtDecode } from 'jwt-decode';
import moment from 'moment';
import { useNavigate } from 'react-router-dom';

const AnimalList = () => {
    const navigate = useNavigate();
    const [data, setData] = useState({
        animals: [],
        isLoaded: false,
        error: ''
    });
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        const currentUser = getCurrentUser();
        const decodedToken = jwtDecode(currentUser.token);
        if (decodedToken.exp && decodedToken.exp - moment().unix() < 10) {
            refreshToken().then(() => {

            }).catch(() => {
                logout();
                navigate('/');
                window.location.reload();
            })
        } else {
            setData({ ...data, isLoaded: false });
            getAnimals(currentPage).then(async (response) => {
                const result = await response.json();
                setData({ ...data, animals: result, isLoaded: true });
                const totalCount = Number(response.headers.get('Animal-Total-Count'));
                setTotalPages(Math.floor((totalCount + 9) / 10));
            }).catch(() => {
                setData({ ...data, error: 'Unable to load data.', isLoaded: true });
            });
        }
    }, [currentPage]);

    const handlePreviousPage = () => {
        if (currentPage > 0) {
            setCurrentPage(currentPage - 1);
        }
    }

    const handleNextPage = () => {
        if (currentPage < totalPages - 1) {
            setCurrentPage(currentPage + 1);
        }
    }

    const handleAnimalAdoption = (animalId) => {
        adoptAnimal(animalId, getCurrentUser().id).then(() => {
            document.getElementById(animalId).textContent = 'Adoption request created successfully.';
            document.getElementById(animalId).style.display = 'block';
            document.getElementById(animalId).style.color = 'green';
        }).catch(() => {
            document.getElementById(animalId).textContent = 'Error occured during adoption request creation.';
            document.getElementById(animalId).style.display = 'block';
            document.getElementById(animalId).style.color = 'red';
        });
    }

    const handleDeleteAnimal = (animalId) => {
        console.log('delete')
        deleteAnimal(animalId).then(() => {
            document.getElementById(animalId).textContent = 'Animal deleted successfully.';
            document.getElementById(animalId).style.display = 'block';
            document.getElementById(animalId).style.color = 'green';
        }).catch(() => {
            document.getElementById(animalId).textContent = 'Error occured during deletion of animal.';
            document.getElementById(animalId).style.display = 'block';
            document.getElementById(animalId).style.color = 'red';
        });
    }

    if (!data.isLoaded) {
        return (
            <div>
                <div>Loading...</div>
            </div>
        );
    }

    if (data.error !== '') {
        return (
            <div>
                <div>{data.error}</div>
            </div>
        );
    }

    const animalList = data.animals.map((animal) => {
        return (
            <Card className="animalDetailsCard" key={animal.id}>
                <Card.Body>
                    <Card.Title className="animalDetailsCardTitle">{animal.name}</Card.Title>
                </Card.Body>
                <ListGroup>
                    <ListGroup.Item>Breed: {animal.breed}</ListGroup.Item>
                    <ListGroup.Item>Age: {animal.age}</ListGroup.Item>
                    <ListGroup.Item>Gender: {animal.gender}</ListGroup.Item>
                    <ListGroup.Item>Weight: {animal.weight}</ListGroup.Item>
                    <ListGroup.Item>Color: {animal.color}</ListGroup.Item>
                    <ListGroup.Item>Arrival Date: {new Date(animal.arrivalDate).toISOString().substring(0, 10)}</ListGroup.Item>
                    <ListGroup.Item>Medical Information: {animal.medicalInformation}</ListGroup.Item>
                    <ListGroup.Item>Special Needs: {animal.specialNeeds}</ListGroup.Item>
                    <ListGroup.Item>Kennel Number: {animal.kennelNumber}</ListGroup.Item>
                    <ListGroup.Item>Adoption Status: {animal.adoptionStatus}</ListGroup.Item>
                    {animal.adoptionStatus === "NOT_ADOPTED" && (
                        <ListGroup.Item className="actionButtonList">
                            <Button variant="primary" className="adoptionButton" href={`animals/add/${animal.id}`}>Edit</Button>
                            <Button variant="danger" className="adoptionButton" onClick={() => handleDeleteAnimal(animal.id)}>Delete</Button>
                        </ListGroup.Item>
                    )}
                </ListGroup>
                <Card.Body className="adoptionCardBody">
                    {animal.adoptionStatus === "NOT_ADOPTED" && <Button variant="success" className="adoptionButton" onClick={() => handleAnimalAdoption(animal.id)}>Adopt</Button>}
                    {animal.adoptionStatus !== "NOT_ADOPTED" && <Card.Text className="adoptedText">Adopted</Card.Text>}
                    <Card.Text id={animal.id} className="feedback"></Card.Text>
                </Card.Body>
            </Card>
        );
    });

    return (
        <div>
            <Container fluid>
                {data.animals.length === 0 && (
                    <div className="nothingDiv">Nothing to show.</div>
                )}
                {data.animals.length > 0 && (
                    <div className="listPaginationContainer">
                        <div className="cardContainer">
                            {animalList}
                        </div>
                        <div className="pagination">
                            <Button variant="primary" className="adoptionButton" onClick={handlePreviousPage} disabled={currentPage === 0}>
                                Previous Page
                            </Button>
                            <Button variant="primary" className="adoptionButton" onClick={handleNextPage} disabled={currentPage === totalPages - 1}>
                                Next Page
                            </Button>
                        </div>
                    </div>
                )}
            </Container>
        </div>
    );
}

export default AnimalList;
