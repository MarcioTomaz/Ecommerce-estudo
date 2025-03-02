import {useEffect, useState} from "react";
import { Select, Checkbox, Card, Button, Grid, Text, Group, Container } from "@mantine/core";
import axios from "axios";
import {API_URL} from "../../hooks/api.jsx";

const mockAddresses = [
    { id: "1", label: "Rua A, 123 - Cidade X" },
    { id: "2", label: "Avenida B, 456 - Cidade Y" },
];

const mockCartItems = [
    { id: "101", name: "Produto 1", quantity: 2, price: 50.0 },
    { id: "102", name: "Produto 2", quantity: 1, price: 30.0 },
];

const PaymentStep = () => {
    const [deliveryAddress, setDeliveryAddress] = useState(null);
    const [billingAddress, setBillingAddress] = useState(null);
    const [sameBilling, setSameBilling] = useState(false);

    useEffect(() => {
        setBillingAddress(axios.get(`${API_URL}/address/read/addresses`),);
    }, []);

    return (
        <Container size="md" style={{ display: "flex", justifyContent: "center", alignItems: "center", }}>
            <Card shadow="sm" padding="lg" style={{ width: "100%", maxWidth: 600 }}>
                <Grid gutter="md">
                    <Grid.Col span={12}>
                        <Text weight={500}>Endereço de Entrega</Text>
                        <Select
                            data={mockAddresses.map((a) => ({ value: a.id, label: a.label }))}
                            value={deliveryAddress}
                            onChange={setDeliveryAddress}
                            placeholder="Selecione o endereço"
                        />
                    </Grid.Col>
                    <Grid.Col span={12}>
                        <Text weight={500}>Endereço de Cobrança</Text>
                        <Checkbox
                            label="Usar o mesmo endereço de entrega"
                            checked={sameBilling}
                            onChange={(event) => {
                                setSameBilling(event.currentTarget.checked);
                                if (event.currentTarget.checked) {
                                    setBillingAddress(deliveryAddress);
                                }
                            }}
                        />
                        {!sameBilling && (
                            <Select
                                data={mockAddresses.map((a) => ({ value: a.id, label: a.label }))}
                                value={billingAddress}
                                onChange={setBillingAddress}
                                placeholder="Selecione o endereço"
                            />
                        )}
                    </Grid.Col>
                </Grid>
                <Text mt="lg" weight={500}>Itens do Pedido</Text>
                {mockCartItems.map((item) => (
                    <Group key={item.id} position="apart">
                        <Text>{item.name} x{item.quantity}</Text>
                        <Text>R$ {item.price.toFixed(2)}</Text>
                    </Group>
                ))}
                <Button mt="lg" fullWidth >
                    Prosseguir para o Pagamento
                </Button>
            </Card>
        </Container>
    );
};

export default PaymentStep;
