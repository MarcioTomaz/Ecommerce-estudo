import React, {useEffect} from 'react';
import {useForm} from '@mantine/form';
import axios from 'axios';
import {TextInput, Select, Button, Textarea, Container, Grid, Title} from '@mantine/core';
import {API_URL} from '../../hooks/api.jsx'; // Corrigido import
import {useNavigate, useParams} from 'react-router-dom'; // Importar useParams

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
            addressType: 'ENTREGA'
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

    const {id} = useParams(); // Obter id da URL
    const navigate = useNavigate();

    useEffect(() => {
        // Função para buscar os dados do endereço
        const fetchAddress = async () => {
            try {
                const response = await axios.get(`${API_URL}/address/read/${id}`); // Usar id da URL
                //  console.log('RESPONSE DATA: ', response.data); // Corrigir o log
                const addressData = response.data;

                // Redefina os valores do formulário com os dados obtidos
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
                    addressType: addressData.addressType || 'ENTREGA'
                });
            } catch (error) {
                console.error('Erro ao buscar dados do endereço:', error);
            }
        };

        // Verificar se o id está presente
        if (id) {
            fetchAddress();
        }
    }, [id]); // Dependência apenas do id

    const handleSubmit = async (values) => {
        //   console.log('HANDLE SUBMIT: ' + JSON.stringify(values));
        try {

            await axios.post(`${API_URL}/address/update/${id}`, values);
            navigate(`/address/list`);

        } catch (error) {
            console.error('Erro ao atualizar dados do endereço:', error);
        }
    };

    return (
        <Container>
            <form onSubmit={form.onSubmit(handleSubmit)}>
                <Title>Endereço</Title>
                <Grid>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Rua"
                            {...form.getInputProps('street')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Número"
                            {...form.getInputProps('number')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Bairro"
                            {...form.getInputProps('district')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="CEP"
                            {...form.getInputProps('zipCode')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Logradouro"
                            {...form.getInputProps('logradouro')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Cidade"
                            {...form.getInputProps('city')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="País"
                            {...form.getInputProps('country')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <TextInput
                            label="Estado"
                            {...form.getInputProps('state')}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <Select
                            label="Tipo de Residência"
                            {...form.getInputProps('residencyType')}
                            data={[
                                {value: 'casa', label: 'Casa'},
                                {value: 'apartamento', label: 'Apartamento'},
                                // Adicione outras opções conforme necessário
                            ]}
                        />
                    </Grid.Col>
                    <Grid.Col span={4}>
                        <Select
                            label="Tipo de Endereço"
                            {...form.getInputProps('addressType')}
                            data={[
                                {value: 'ENTREGA', label: 'Entrega'},
                                {value: 'COBRANCA', label: 'Cobrança'},
                                // Adicione outras opções conforme necessário
                            ]}
                        />
                    </Grid.Col>
                    <Grid.Col span={12}>
                        <Textarea
                            label="Observações"
                            {...form.getInputProps('observation')}
                        />
                    </Grid.Col>
                </Grid>
                <Button type="submit" mt="md">Salvar</Button>
            </form>
        </Container>
    );
};

export default AddressForm;
