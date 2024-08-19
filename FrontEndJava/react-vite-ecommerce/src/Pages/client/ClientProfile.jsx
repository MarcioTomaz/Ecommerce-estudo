import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Container, Title, Text, Loader, Alert, Button, Grid} from '@mantine/core';
import {API_URL} from "../../hooks/api.jsx";
import {useNavigate} from "react-router-dom";
import {ROUTES} from "../../routes/URLS.jsx";

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

    const changePage = (page) => {
        navigate(`${page}`);
    }

    if (isLoading) return <Loader/>;

    if (error) return <Alert title="Erro" color="red">Erro ao carregar os dados: {error.message}</Alert>;
    ''
    return (
        <Container>
            <Grid>
                <Grid.Col span={12}>
                    <Title order={2}>Perfil do Usuário</Title>
                </Grid.Col>
                <Grid.Col span={4}>
                    <Text><strong>Nome: {userData?.person.firstName} {userData?.person.lastName}</strong></Text>
                </Grid.Col>
                <Grid.Col span={4}>
                    <Text><strong>Email:</strong> {userData?.email}</Text>
                </Grid.Col>
                <Grid.Col span={4}>
                    <Text><strong>Data de Nascimento:</strong> {userData?.person.birthDate}</Text>
                </Grid.Col>
                <Grid.Col span={4}>
                    <Text><strong>Sexo:</strong> {userData?.person.gender}</Text>
                </Grid.Col>
                <Grid.Col span={4}>
                    <Text><strong>Telefone:</strong> {userData?.person.phoneType}: {userData?.person.phoneNumber}
                    </Text>
                </Grid.Col>
            </Grid>

            <Container>
                <Grid>
                    <Grid.Col span={2}>
                        <Button
                            variant="filled"
                            color="cyan"
                            radius="md"
                            onClick={() => changePage(ROUTES.ADDRESS_LIST)}>
                            Endereços
                        </Button>
                    </Grid.Col>
                    <Grid.Col span={2}>
                        <Button
                            variant="filled"
                            color="cyan"
                            radius="md"
                            onClick={() => changePage(ROUTES.CARD_LIST)}>
                            Cartões
                        </Button>
                    </Grid.Col>
                </Grid>
            </Container>
        </Container>
    )
        ;
};

export default ClientProfile;
