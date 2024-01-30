import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import { NavLink } from 'react-router-dom';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Container from 'react-bootstrap/Container';
import { logout } from '../services/AuthService';

const NavBar = (props) => {
    const user = props.user;

    const handleLogout = () => {
        logout();
        window.location.reload();
    }
    return (
        <Navbar expand="lg" className="navbar">
            <Container className="navbarContainer">
                <Navbar.Brand as={NavLink} to="/" className="navLink">Pet Adoption App</Navbar.Brand>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="navLinkContainer" >
                        <Nav.Link as={NavLink} to="/" className="navLink">Home</Nav.Link>
                        {user.isUser === true && (<NavDropdown title={<span className="navLink">Animals</span>}>
                            <NavDropdown.Item as={NavLink} to="/animals">View Animals</NavDropdown.Item>
                            {user.isAdmin == true && (<NavDropdown.Item as={NavLink} to="/animals/add/new">Add New Animal</NavDropdown.Item>)}
                        </NavDropdown>
                        )}
                        {user.isAdmin && (<Nav.Link as={NavLink} to="/adoptions" className="navLink">Adoptions</Nav.Link>)}
                        {!user.user && (<Nav.Link as={NavLink} to="/login" className="navLink">Login</Nav.Link>)}
                        {!user.user && (<Nav.Link as={NavLink} to="/signup" className="navLink">Sign up</Nav.Link>)}
                        {user.user && (<Nav.Link to="/" as={NavLink} className="navLink" onClick={() => handleLogout()}>Logout</Nav.Link>)}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )

}

export default NavBar;