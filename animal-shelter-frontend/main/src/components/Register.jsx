import React, { useEffect, useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import { register } from '../services/AuthService';
import { useNavigate } from "react-router-dom";

const Register = () => {
    const navigate = useNavigate();
    const [credentials, setCredentials] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [feedback, setFeedback] = useState('');

    const setUsername = (username) => {
        setCredentials({ ...credentials, username });
    };

    const setEmail = (email) => {
        setCredentials({ ...credentials, email });
    };

    const setPassword = (password) => {
        setCredentials({ ...credentials, password });
    };

    useEffect(() => {
        validateForm();
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
        validateForm();
        register(credentials)
            .then(() => {
                navigate("/login");
                window.location.reload();
            })
            .catch((error) => {
                setFeedback(error.message);
            });
    };

    return (
        <div>
            <Container fluid>
                <div className="loginDiv">
                    <form className="needs-validation" onSubmit={handleSubmit} noValidate>
                        <div className="form-group col-md-6">
                            <label htmlFor="email-label">Email</label>
                            <input type="email" className="form-control" id="email-input" required onChange={(e) => setEmail(e.target.value)} value={credentials.email} />
                            <div className="invalid-feedback">Please enter a valid email!</div>
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="form-group col-md-6">
                            <label htmlFor="username-label">Username</label>
                            <input type="text" className="form-control" id="username-input" required onChange={(e) => setUsername(e.target.value)} value={credentials.username} />
                            <div className="invalid-feedback">Please enter a valid username!</div>
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="form-group col-md-6">
                            <label htmlFor="password-label">Password</label>
                            <input type="password" className="form-control" id="password-input" required onChange={(e) => setPassword(e.target.value)} value={credentials.password} />
                            <div className="invalid-feedback">Please enter a valid password!</div>
                            <div className="valid-feedback">Looks good!</div>
                        </div>
                        <div className="loginButtonDiv">
                            <Button variant="primary" type="submit" className="loginButton">
                                Login
                            </Button>
                        </div>
                        {feedback && (<div id="loginFeedback" className="feedbackDiv">{feedback}</div>)}
                    </form>
                </div>
            </Container>
        </div>
    );
};

export default Register;
