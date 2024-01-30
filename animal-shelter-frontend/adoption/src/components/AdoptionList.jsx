import React, { useEffect, useState } from 'react';
import { Container, Table } from 'react-bootstrap';
import { getAdoptions } from '../services/AdoptionService';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import { reactToAdoptionRequest } from '../services/AdoptionService';
import { jwtDecode } from 'jwt-decode';
import moment from 'moment';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser } from '../services/AuthService';

const AdoptionList = () => {
    const navigate = useNavigate();
    const [data, setData] = useState({
        adoptions: [],
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
                setData({ ...data, isLoaded: false });
                getAdoptions(currentPage).then(async (response) => {
                    const result = await response.json();
                    setData({ ...data, adoptions: result, isLoaded: true });
                    const totalCount = Number(response.headers.get('Adoption-Total-Count'));
                    setTotalPages(Math.floor((totalCount + 9) / 10));
                }).catch(() => {
                    setData({ ...data, error: 'Unable to load data.', isLoaded: true });
                });
            }).catch(() => {
                logout();
                navigate('/');
                window.location.reload();
            })
        } else {
            setData({ ...data, isLoaded: false });
            getAdoptions(currentPage).then(async (response) => {
                const result = await response.json();
                setData({ ...data, adoptions: result, isLoaded: true });
                const totalCount = Number(response.headers.get('Adoption-Total-Count'));
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

    const reactToAnimalAdoptionRequest = (id, reaction) => {
        const currentUser = getCurrentUser();
        const decodedToken = jwtDecode(currentUser.token);
        if (decodedToken.exp && decodedToken.exp - moment().unix() < 10) {
            refreshToken().then(() => {
                reactToAdoptionRequest(id, reaction).then(() => {
                    document.getElementById(id).textContent = reaction === "accept" ? 'Adoption request accepted' : 'Adoption request rejected';
                    document.getElementById(id).style.display = 'block';
                    document.getElementById(id).style.color = 'green';
                }).catch(() => {
                    document.getElementById(id).textContent = 'Error occured during adoption request creation.';
                    document.getElementById(id).style.display = 'block';
                    document.getElementById(id).style.color = 'red';
                });
            }).catch(() => {
                logout();
                navigate('/');
                window.location.reload();
            })
        } else {
            reactToAdoptionRequest(id, reaction).then(() => {
                document.getElementById(id).textContent = reaction === "accept" ? 'Adoption request accepted' : 'Adoption request rejected';
                document.getElementById(id).style.display = 'block';
                document.getElementById(id).style.color = 'green';
            }).catch(() => {
                document.getElementById(id).textContent = 'Error occured during adoption request creation.';
                document.getElementById(id).style.display = 'block';
                document.getElementById(id).style.color = 'red';
            });
        }
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

    const adoptionList = data.adoptions.map((adoption) => {
        return (
            <Card className="adoptionDetailsCard" key={adoption.id}>
                <Card.Body>
                    <Card.Title className="adoptionDetailsCardTitle">Adoption #{adoption.id}</Card.Title>
                </Card.Body>
                <ListGroup>
                    <ListGroup.Item>Username: {adoption.user.username}</ListGroup.Item>
                    <ListGroup.Item>Email: {adoption.user.email}</ListGroup.Item>
                    <ListGroup.Item>Animal name: {adoption.animal.name}</ListGroup.Item>
                    <ListGroup.Item>Animal Breed: {adoption.animal.breed}</ListGroup.Item>
                    <ListGroup.Item>Request Date: {new Date(adoption.requestDate).toISOString()}</ListGroup.Item>
                </ListGroup>
                <Card.Body className="adoptionCardBody">
                    <div className="adoptionReactionBody">
                        <Button variant="success" className="adoptionReactionButton" onClick={() => reactToAnimalAdoptionRequest(adoption.id, "accept")}>Accept</Button>
                        <Button variant="danger" className="adoptionReactionButton" onClick={() => reactToAnimalAdoptionRequest(adoption.id, "reject")}>Reject</Button>
                    </div>
                    <Card.Text id={adoption.id} className="adoptionFeedback"></Card.Text>
                </Card.Body>
            </Card>
        );
    });

    return (
        <div>
            <Container fluid>
                {data.adoptions.length === 0 && (
                    <div className="nothingDiv">Nothing to show.</div>
                )}
                {data.adoptions.length > 0 && (
                    <div className="listPaginationContainer">
                        <div className="cardContainer">
                            {adoptionList}
                        </div>
                        <div className="pagination">
                            <Button variant="primary" className="adoptionReactionButton" onClick={handlePreviousPage} disabled={currentPage === 0}>
                                Previous Page
                            </Button>
                            <Button variant="primary" className="adoptionReactionButton" onClick={handleNextPage} disabled={currentPage === totalPages - 1}>
                                Next Page
                            </Button>
                        </div>
                    </div>
                )}
            </Container>
        </div>
    );
}

export default AdoptionList;
