import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {useMantineTheme, Table, Container, Button, Modal, Text, Group} from "@mantine/core";
import axios from "axios";
import { API_URL } from "../../hooks/api.jsx";
import { ROUTES } from "../../routes/URLS.jsx";

const CardList = () => {
    const [userId, setUserId] = useState(null);
    const [userData, setUserData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedCardId, setSelectedCardId] = useState(null);
    const [opened, setOpened] = useState(false);



    const navigate = useNavigate();
    const theme = useMantineTheme();

    useEffect(() => {
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
            axios.get(`${API_URL}/card/read/card/${userId}`)
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

    const confirmDeleteCard = (id) => {
        setSelectedCardId(id);
        setOpened(true);
    }

    const rows = userData.map((row) => (
        <Table.Tr key={row.id}>
            <Table.Td>{row.alias}</Table.Td>
            <Table.Td>{row.flag}</Table.Td>
            <Table.Td>{row.id}</Table.Td>
            <Table.Td>{row.number}</Table.Td>
            <Table.Td>{row.holder}</Table.Td>
            <Table.Td>{row.expirationDate}</Table.Td>
            <Table.Td>
                <Button style={{background: theme.colors.red[9]}}
                        onClick={() => confirmDeleteCard(row.id)}>Excluir</Button>
            </Table.Td>
        </Table.Tr>
    ));

    const deleteCard = () => {
        axios.delete(`${API_URL}/card/delete/${selectedCardId}`)
            .then(() => {
                setUserData(userData.filter((address) => address.id !== selectedCardId));
                setOpened(false);
            })
            .catch(error => {
                setError(error);
                setOpened(false);
            });
    }

    return (
        <Container>
            <Button onClick={() => navigate(ROUTES.CARD_REGISTER)}>Cadastrar novo Cartão</Button>

            <Table miw={700}>
                <Table.Thead>
                    <Table.Tr>
                        <Table.Th>Alias</Table.Th>
                        <Table.Th>Flag</Table.Th>
                        <Table.Th>Id</Table.Th>
                        <Table.Th>Number</Table.Th>
                        <Table.Th>Holder</Table.Th>
                        <Table.Th>Expiration Date</Table.Th>
                    </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                    {isLoading ? (
                        <Table.Tr>
                            <Table.Td colSpan={6}>Loading...</Table.Td>
                        </Table.Tr>
                    ) : error ? (
                        <Table.Tr>
                            <Table.Td colSpan={6}>Error loading data</Table.Td>
                        </Table.Tr>
                    ) : rows.length > 0 ? (
                        rows
                    ) : (
                        <Table.Tr>
                            <Table.Td colSpan={6}>No data available</Table.Td>
                        </Table.Tr>
                    )}
                </Table.Tbody>
            </Table>

            <Modal
                opened={opened}
                onClose={() => setOpened(false)}
                title="Confirmar Exclusão"
                centered
            >
                <Text>Você tem certeza de que deseja excluir este Cartão? Esta ação não pode ser desfeita.</Text>

                <Group position="apart" mt="md">
                    <Button onClick={() => setOpened(false)}>Cancelar</Button>
                    <Button color="red" onClick={deleteCard}>Confirmar Exclusão</Button>
                </Group>
            </Modal>
            <Button style={{background: theme.colors.yellow[9]}} onClick={() => navigate('/profile')} type="button"
                    mt="md">Voltar</Button>
        </Container>
    );
};

export default CardList;

