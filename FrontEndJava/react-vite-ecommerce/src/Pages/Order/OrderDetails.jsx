import {
    Card,
    Text,
    Grid,
    Title,
    Divider,
    Group,
    Badge,
    Button,
    Collapse,
    Container,
    useMantineTheme
} from '@mantine/core';
import React, {useContext, useEffect, useState} from 'react';
import {AuthContext} from "../../GlobalConfig/AuthContext.jsx";
import {API_URL} from "../../hooks/api.jsx";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {ROUTES} from "../../routes/URLS.jsx";

const OrderDetails = () => {
    const [itemsOpen, setItemsOpen] = useState(false);
    const { login, userToken } = useContext(AuthContext);

    const [orderDetails, setOrderDetails] = useState(null);
    const { id } = useParams();
    const navigate = useNavigate();
    const theme = useMantineTheme();

    useEffect(() => {
        axios.get(`${API_URL}/order/details/${id}`,
            {headers:{'Authorization': `Bearer ${userToken}`}})
            .then(res => {
                setOrderDetails(res.data);
                console.log(res.data);
            })
    }, [id, userToken]);

    return (
        <Container size="md" style={{marginTop: 40}}>

            <Card shadow="sm" padding="lg" radius="md" withBorder>
                <Title order={2}>Detalhes do Pedido</Title>
                <Divider my="sm"/>

                {/* Informações do Cliente */}
                <Title order={4}>Cliente</Title>
                <Text>
                    {orderDetails?.person.firstName} {orderDetails?.person.lastName} ({orderDetails?.person.gender})
                </Text>
                <Text>Data de Nascimento: {orderDetails?.person.birthDate}</Text>
                <Text>Telefone: {orderDetails?.person.phoneNumber} ({orderDetails?.person.phoneType})</Text>

                <Divider my="sm"/>

                {/* Endereço de Cobrança */}
                <Title order={4}>Endereço de Cobrança</Title>
                <Text>
                    {orderDetails?.billingAddress.street}, {orderDetails?.billingAddress.number} - {orderDetails?.billingAddress.district}
                </Text>
                <Text>
                    {orderDetails?.billingAddress.city} - {orderDetails?.billingAddress.state}, {orderDetails?.billingAddress.zipCode}
                </Text>
                <Text>País: {orderDetails?.billingAddress.country}</Text>

                <Divider my="sm"/>

                {/* Endereço de Entrega */}
                <Title order={4}>Endereço de Entrega</Title>
                <Text>
                    {orderDetails?.shippingAddress.street}, {orderDetails?.shippingAddress.number} - {orderDetails?.shippingAddress.district}
                </Text>
                <Text>
                    {orderDetails?.shippingAddress.city} - {orderDetails?.shippingAddress.state}, {orderDetails?.shippingAddress.zipCode}
                </Text>
                <Text>País: {orderDetails?.shippingAddress.country}</Text>

                <Divider my="sm"/>

                {/* Itens do Pedido */}
                <Title order={4}>Itens do Pedido</Title>
                <Button onClick={() => setItemsOpen((prev) => !prev)}>
                    {itemsOpen ? 'Ocultar Itens' : 'Mostrar Itens'}
                </Button>
                <Collapse in={itemsOpen}>
                    {orderDetails?.orderItemsDTO.map((item, index) => (
                        <Card key={index} shadow="xs" padding="sm" mt="sm">
                            <Text>Produto: {item.product.product_name}</Text>
                            <Text>Descrição: {item.product.product_description}</Text>
                            <Text>Categoria: {item.product.productCategory}</Text>
                            <Text>Preço: R$ {item.product.product_price.toFixed(2)}</Text>
                            <Text>Quantidade: {item.quantity}</Text>
                        </Card>
                    ))}
                </Collapse>

                <Divider my="sm"/>

                {/* Pagamento */}
                <Title order={4}>Pagamento</Title>

                {orderDetails?.status !== 'PAID' ? <Button onClick={() => navigate(`/order/${id}/payment`)} type="button" >Ir para pagamento</Button> : ''}

                <Text>Total: R$ {orderDetails?.cart.toFixed(2)}</Text>
                <Group>
                    <Text>Método: {orderDetails?.paymentMethods[0]?.type}</Text>
                    <Badge color={orderDetails?.status === 'PAID' ? 'green' : 'red'}>{orderDetails?.status}</Badge>
                </Group>
            </Card>
            <Button
                style={{background: theme.colors.yellow[9]}}
                onClick={() => navigate(ROUTES.PROFILE)}
                type="button"
                mt="md">Voltar
            </Button>
        </Container>
    );
};

export default OrderDetails;
