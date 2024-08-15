import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Container, Title, Text, Loader, Alert, Button} from '@mantine/core';
import {API_URL} from "../../hooks/api.jsx";
import {useNavigate} from "react-router-dom";

const ClientProfile = () => {
    const [userId, setUserId] = useState(null);
    const [userData, setUserData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    useEffect(() => {
        // Recupera o ID do usuário do localStorage
        const storedUser = localStorage.getItem('userLogin');
        if (storedUser) {
            const parsedValue = JSON.parse(storedUser);
            const userID = parsedValue.id;
            if (userID) {
                setUserId(userID);
            }
        }
    }, []);

    useEffect(() => {
        if (userId) {
            setIsLoading(true);
            axios.get(`${API_URL}/userPerson/read/${userId}`)
                .then(response => {
                    setUserData(response.data);
                    setIsLoading(false);
                })
                .catch(error => {
                    setError(error);
                    setIsLoading(false);
                });
        }
    }, [userId]);

    const changePage = (page) =>{
        navigate(`${page}`);
    }

    if (isLoading) return <Loader/>;

    if (error) return <Alert title="Erro" color="red">Erro ao carregar os dados: {error.message}</Alert>;
    ''
    return (
        <Container>
            <Title order={2}>Perfil do Usuário</Title>
            <Text><strong>Nome: {userData?.person.firstName} {userData?.person.lastName}</strong></Text>
            <Text><strong>Email:</strong> {userData?.email}</Text>
            <Text><strong>Data de Nascimento:</strong> {userData?.person.birthDate}</Text>

            <Container>
                <Button
                    variant="filled"
                    color="cyan"
                    radius="md"
                    onClick={ () => changePage('/address/list')}>
                    Endereços</Button>
            </Container>
        </Container>
    );
};

export default ClientProfile;
