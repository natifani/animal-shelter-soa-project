import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Button } from 'react-bootstrap';
import { getAnimalById } from '../services/AnimalService';
import { putAnimal } from '../services/AnimalService';
import { addNewAnimal } from '../services/AnimalService';
import { jwtDecode } from 'jwt-decode';
import moment from 'moment';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser } from '../services/AuthService';

const AddAnimal = () => {
    const navigate = useNavigate();
    const [animal, setAnimal] = useState({
        name: '',
        breed: '',
        age: '',
        gender: '',
        weight: '',
        color: '',
        arrivalDate: '',
        medicalInformation: '',
        specialNeeds: '',
        kennelNumber: ''
    });
    const [feedback, setFeedback] = useState('');
    const [loaded, setLoaded] = useState(true);
    const { id } = useParams();

    const setName = (name) => {
        setAnimal({ ...animal, name });
    };

    const setBreed = (breed) => {
        setAnimal({ ...animal, breed });
    };

    const setAge = (age) => {
        setAnimal({ ...animal, age });
    }

    const setGender = (gender) => {
        setAnimal({ ...animal, gender });
    };

    const setWeight = (weight) => {
        setAnimal({ ...animal, weight });
    };

    const setColor = (color) => {
        setAnimal({ ...animal, color });
    };

    const setArrivalDate = (arrivalDate) => {
        setAnimal({ ...animal, arrivalDate });
    };

    const setMedicalInformation = (medicalInformation) => {
        setAnimal({ ...animal, medicalInformation });
    };

    const setSpecialNeeds = (specialNeeds) => {
        setAnimal({ ...animal, specialNeeds });
    };

    const setKennelNumber = (kennelNumber) => {
        setAnimal({ ...animal, kennelNumber });
    };

    useEffect(() => {
        if (loaded) {
            validateForm();
        }
    }, [loaded]);

    useEffect(() => {
        if (id !== 'new') {
            const currentUser = getCurrentUser();
            const decodedToken = jwtDecode(currentUser.token);
            if (decodedToken.exp && decodedToken.exp - moment().unix() < 10) {
                refreshToken().then(() => {
                    setLoaded(false);
                    getAnimalById(id)
                        .then((result) => {
                            result.arrivalDate = new Date(result.arrivalDate).toISOString().substring(0, 10);
                            setAnimal({ ...result });
                            setLoaded(true);
                        })
                        .catch(() => {
                            console.log('Unable to load data.');
                            setLoaded(true);
                            setFeedback('Unable to load data.');
                        })
                }).catch(() => {
                    logout();
                    navigate('/');
                    window.location.reload();
                })
            } else {
                setLoaded(false);
                getAnimalById(id)
                    .then((result) => {
                        result.arrivalDate = new Date(result.arrivalDate).toISOString().substring(0, 10);
                        setAnimal({ ...result });
                        setLoaded(true);
                    })
                    .catch(() => {
                        console.log('Unable to load data.');
                        setLoaded(true);
                        setFeedback('Unable to load data.');
                    })
            }
        }
    }, []);

    function validateForm() {
        var forms = document.querySelectorAll('.needs-validation');

        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener(
                'submit',
                function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                },
                false
            );
        });
    }

    function resetForm() {
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.classList.remove('was-validated');
        });
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        const currentUser = getCurrentUser();
        const decodedToken = jwtDecode(currentUser.token);
        if (decodedToken.exp && decodedToken.exp - moment().unix() < 10) {
            refreshToken().then(() => {
                if (id === 'new') {
                    animal['adoptionStatus'] = 'NOT_ADOPTED';
                    addNewAnimal(animal)
                        .then((response) => {
                            if (response.status === 201) {
                                resetForm();
                                setAnimal({
                                    name: '',
                                    breed: '',
                                    age: '',
                                    gender: '',
                                    weight: '',
                                    color: '',
                                    arrivalDate: '',
                                    medicalInformation: '',
                                    specialNeeds: '',
                                    kennelNumber: '',
                                });
                                setFeedback('Animal successfully saved!');
                            } else {
                                if (response.status === 400) {
                                    setFeedback('Could not save animal!');
                                }
                            }
                        })
                        .catch(() => {
                            setFeedback('Could not save animal!');
                        });
                } else {
                    putAnimal(animal, id)
                        .then(() => {
                            resetForm();
                            setFeedback('Animal successfully updated!');
                        })
                        .catch((error) => {
                            setFeedback(`Could not update animal: ${error}`);
                        });
                }
            }).catch(() => {
                logout();
                navigate('/');
                window.location.reload();
            })
        } else {
            if (id === 'new') {
                animal['adoptionStatus'] = 'NOT_ADOPTED';
                addNewAnimal(animal)
                    .then((response) => {
                        if (response.status === 201) {
                            resetForm();
                            setAnimal({
                                name: '',
                                breed: '',
                                age: '',
                                gender: '',
                                weight: '',
                                color: '',
                                arrivalDate: '',
                                medicalInformation: '',
                                specialNeeds: '',
                                kennelNumber: '',
                            });
                            setFeedback('Animal successfully saved!');
                        } else {
                            if (response.status === 400) {
                                setFeedback('Could not save animal!');
                            }
                        }
                    })
                    .catch(() => {
                        setFeedback('Could not save animal!');
                    });
            } else {
                putAnimal(animal, id)
                    .then(() => {
                        resetForm();
                        setFeedback('Animal successfully updated!');
                    })
                    .catch((error) => {
                        setFeedback(`Could not update animal: ${error}`);
                    });
            }
        }
    }

    return (
        <div>
            <Container fluid>
                {!loaded && (
                    <div>Loading...</div>
                )}
                {loaded && (
                    <div className="addAnimalDiv">
                        {id === 'new' && (<h2>Add Animal</h2>)}
                        {id !== 'new' && (<h2>Edit Animal</h2>)}
                        <form className="needs-validation" onSubmit={handleSubmit} noValidate>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="name-label">Name</label>
                                <input type="text" maxLength="255" className="form-control" id="name-input" required onChange={(e) => setName(e.target.value)} value={animal.name} />
                                <div className="invalid-feedback">Please enter a valid name!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="breed-label">Breed</label>
                                <input type="text" maxLength="255" className="form-control" id="breed-input" required onChange={(e) => setBreed(e.target.value)} value={animal.breed} />
                                <div className="invalid-feedback">Please enter a valid breed!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="age-label">Age</label>
                                <input type="number" step='1' min='0' className="form-control" id="age-input" onChange={(e) => setAge(e.target.value)} value={animal.age} />
                                <div className="invalid-feedback">Please enter a valid age!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="gender-label">Gender</label>
                                <select className="form-select" id="gender-select" required onChange={(e) => setGender(e.target.value)}>
                                    <option value="">Choose...</option>
                                    {id !== 'new' && animal.gender === 'MALE' && (
                                        <option value="MALE" selected>MALE</option>
                                    )}
                                    {id !== 'new' && animal.gender !== 'MALE' && (
                                        <option value="MALE">MALE</option>
                                    )}
                                    {id !== 'new' && animal.gender === 'FEMALE' && (
                                        <option value="FEMALE" selected>FEMALE</option>
                                    )}
                                    {id !== 'new' && animal.gender !== 'FEMALE' && (
                                        <option value="FEMALE">FEMALE</option>
                                    )}
                                    {id === 'new' && (
                                        <option value="MALE">MALE</option>
                                    )}
                                    {id === 'new' && (
                                        <option value="FEMALE">FEMALE</option>
                                    )}
                                </select>
                                <div className="invalid-feedback">Please select a valid gender!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="weight-label">Weight</label>
                                <input type="number" min='0' step='0.01' className="form-control" id="weight-input" required onChange={(e) => setWeight(e.target.value)} value={animal.weight} />
                                <div className="invalid-feedback">Please enter a valid weight!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="color-label">Color</label>
                                <input type="text" maxLength="255" className="form-control" id="color-input" required onChange={(e) => setColor(e.target.value)} value={animal.color} />
                                <div className="invalid-feedback">Please enter a valid color!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="arrivaldate-label">Arrival Date</label>
                                <input type="date" className="form-control" id="arrivaldate-input" required onChange={(e) => setArrivalDate(e.target.value)} value={animal.arrivalDate} />
                                <div className="invalid-feedback">Please enter a valid arrival date!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="medicalinformation-label">Medical Information</label>
                                <textarea
                                    className="form-control"
                                    id="medicalinformation"
                                    maxLength="511"
                                    rows="3"
                                    onChange={(e) => setMedicalInformation(e.target.value)}
                                    value={animal.medicalInformation}
                                />
                                <div className="invalid-feedback">Please enter a valid medical information!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="specialneeds-label">Special Needs</label>
                                <textarea
                                    className="form-control"
                                    id="specialneeds"
                                    maxLength="511"
                                    rows="3"
                                    onChange={(e) => setSpecialNeeds(e.target.value)}
                                    value={animal.specialNeeds}
                                />
                                <div className="invalid-feedback">Please enter a valid special need!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="formgroupDiv form-group col-md-6">
                                <label htmlFor="kennelnumber-label">Kennel Number</label>
                                <input type="number" step='1' min='0' className="form-control" id="kennelnumber-input" onChange={(e) => setKennelNumber(e.target.value)} value={animal.kennelNumber} />
                                <div className="invalid-feedback">Please enter a valid kennel number!</div>
                                <div className="valid-feedback">Looks good!</div>
                            </div>
                            <div className="submitButtonDiv form-group col-md-6">
                                <Button variant="primary" type="submit" className="saveAnimalButton">
                                    Save Animal
                                </Button>
                                {feedback && (<div id="animalUpdateFeedback" className="feedbackDiv">{feedback}</div>)}
                            </div>
                        </form>
                    </div>
                )}
            </Container>
        </div>
    );
};

export default AddAnimal;
