import React, {useEffect} from 'react';
import {useForm} from '@mantine/form';
import axios from 'axios';
import {TextInput, Select, Button, Textarea, Container, Grid, Title, useMantineTheme, Paper} from '@mantine/core';
import {API_URL} from '../../hooks/api.jsx';
import {useNavigate, useParams} from 'react-router-dom';
import {ROUTES} from "../../routes/URLS.jsx";

const AddressForm = () => {
    const form = useForm({
        initialValues: {
            street: '',
            residencyType: '',
            observation: '',
            number: '',
            district: '',
            zipCode: '',
            logradouro: '',
            city: '',
            country: '',
            state: '',
            addressType: 'ENTREGA',
            person: {
                id: '' // Inicialmente vazio
            }
        },
        validate: {
            street: (value) => (value.trim() ? null : 'Rua não pode ser nula'),
            residencyType: (value) => (value.trim() ? null : 'Tipo de Residência não pode ser nulo'),
            number: (value) => (value.trim() ? null : 'Número não pode ser nulo'),
            district: (value) => (value.trim() ? null : 'Bairro não pode ser nulo'),
            zipCode: (value) => (value.trim() ? null : 'CEP não pode ser nulo'),
            logradouro: (value) => (value.trim() ? null : 'Logradouro não pode ser nulo'),
            city: (value) => (value.trim() ? null : 'Cidade não pode ser nula'),
            country: (value) => (value.trim() ? null : 'País não pode ser nulo'),
            state: (value) => (value.trim() ? null : 'Estado não pode ser nulo'),
            addressType: (value) => (value.trim() ? null : 'Tipo de Endereço não pode ser nulo')
        }
    });

    const {id} = useParams();
    const navigate = useNavigate();
    const theme = useMantineTheme();

    useEffect(() => {
        const fetchAddress = async () => {
            const storedUser = localStorage.getItem('userLogin');
            let userId;

            if (storedUser) {
                const parsedValue = JSON.parse(storedUser);
                userId = parsedValue?.id;
                if (userId) {
                    form.setFieldValue('person.id', Number(userId));
                }
            }

            try {
                const response = await axios.get(`${API_URL}/address/read/${id}`);
                const addressData = response.data;

                form.setValues({
                    street: addressData.street || '',
                    residencyType: addressData.residencyType || '',
                    observation: addressData.observation || '',
                    number: addressData.number || '',
                    district: addressData.district || '',
                    zipCode: addressData.zipCode || '',
                    logradouro: addressData.logradouro || '',
                    city: addressData.city || '',
                    country: addressData.country || '',
                    state: addressData.state || '',
                    addressType: addressData.addressType || 'ENTREGA',
                    person: {
                        id: userId || '' // Garantir que id seja definido
                    }
                });
            } catch (error) {
                console.error('Erro ao buscar dados do endereço:', error);
            }
        };

        if (id) {
            fetchAddress();
        }
    }, [id]);

    const getTranslation = (key) => {
        const translations = {
            EN: {
                delivery: 'Delivery',
                billing: 'Billing',
                house: 'House',
                apartment: 'Apartment',
            },
            PT: {
                delivery: 'Entrega',
                billing: 'Cobrança',
                house: 'Casa',
                apartment: 'Apartamento',
            },
        };
        const currentLanguage = 'PT'; // Definir como variável global ao refatorar o front

        return translations[currentLanguage][key];
    };

    const residencyTypeOptions = [
        {value: 'CASA', label: getTranslation('house')},
        {value: 'APARTAMENTO', label: getTranslation('apartment')}
    ];

    const addressTypeOptions = [
        {value: 'ENTREGA', label: getTranslation('delivery')},
        {value: 'COBRANCA', label: getTranslation('billing')}
    ];

    const handleSubmit = async (values) => {
        console.log('HANDLE SUBMIT: ' + JSON.stringify(values));
        try {
            await axios.post(`${API_URL}/address/update/${id}`, values);
            navigate(ROUTES.ADDRESS_LIST);
        } catch (error) {
            console.error('Erro ao atualizar dados do endereço:', error);
        }
    };

    return (
        <Container>
            <Paper withBorder shadow="md" p={30} mt={30} radius="md">

                <form onSubmit={form.onSubmit(handleSubmit)}>
                    <Title>Endereço</Title>
                    <Grid>
                        <Grid.Col span={4}>
                            <TextInput label="Rua" {...form.getInputProps('street')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="Número" {...form.getInputProps('number')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="Bairro" {...form.getInputProps('district')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="CEP" {...form.getInputProps('zipCode')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="Logradouro" {...form.getInputProps('logradouro')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="Cidade" {...form.getInputProps('city')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="País" {...form.getInputProps('country')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <TextInput label="Estado" {...form.getInputProps('state')} />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <Select
                                label="Tipo de Residência"
                                {...form.getInputProps('residencyType')}
                                data={residencyTypeOptions}
                            />
                        </Grid.Col>
                        <Grid.Col span={4}>
                            <Select
                                label="Tipo de Endereço"
                                {...form.getInputProps('addressType')}
                                data={addressTypeOptions}
                            />
                        </Grid.Col>
                        <Grid.Col span={12}>
                            <Textarea label="Observações" {...form.getInputProps('observation')} />
                        </Grid.Col>
                    </Grid>
                    <Grid>
                        <Grid.Col span={6}>
                            <Button type="submit" mt="md">Salvar</Button>
                        </Grid.Col>
                        <Grid.Col span={6}>
                            <Button
                                style={{background: theme.colors.yellow[9]}}
                                onClick={() => navigate(ROUTES.ADDRESS_LIST)}
                                type="button"
                                mt="md"
                            >
                                Voltar
                            </Button>
                        </Grid.Col>
                    </Grid>
                </form>
            </Paper>
        </Container>
    );
};

export default AddressForm;
