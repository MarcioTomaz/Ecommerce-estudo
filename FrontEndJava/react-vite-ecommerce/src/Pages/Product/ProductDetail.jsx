import {Container, Grid, Paper, Title, Text, Loader, Alert, Textarea, Button} from "@mantine/core";
import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {API_URL} from "../../hooks/api.jsx";
import {ROUTES} from "../../routes/URLS.jsx";

const ProductDetail = () => {
    const [userId, setUserId] = useState(null);
    const [productData, setproductData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const {id} = useParams();

    useEffect(() => {
        // Recupera o ID do usuário do localStorage
        const storedUser = localStorage.getItem("userLogin");
        if (storedUser) {
            const parsedValue = JSON.parse(storedUser);
            const userID = parsedValue.id;
            if (userID) {
                setUserId(userID);
            }
        }
    }, []);

    useEffect(() => {
        axios
            .get(`${API_URL}${ROUTES.PRODUCT_READ_ID}/${id}`)
            .then((response) => {
                setproductData(response.data);
                setIsLoading(false);
                console.log("PRODUTO: ", response.data);
            })
            .catch((error) => {
                setError(error);
                setIsLoading(false);
            });
    }, [id]);

    // if (isLoading) return <Loader/>;

    // if (error) return <Alert title="Erro" color="red">Erro ao carregar os dados: {error.message}</Alert>;
    // ''

    return (
        <Container size="lg">
            <Paper padding="md">
                <Grid>

                    <Grid.Col span={12}>
                        <Title order={2}>{productData?.product_name}</Title>
                        <Text>Detalhes do Produto</Text>
                    </Grid.Col>

                    <Grid.Col
                        span={6}
                        style={{
                            border: "2px solid blue",
                            borderRadius: "8px",
                            padding: "20px",
                            height: "100%",
                        }}
                    >
                        Imagem do produto
                    </Grid.Col>

                    <Grid.Col
                        span={6}
                        style={{
                            border: "2px solid red",
                            borderRadius: "8px",
                            padding: "20px",
                            display: "flex",
                            flexDirection: "column",
                            gap: "10px",
                            height: "100%",
                        }}
                    >
                        <Text>Informações</Text>
                        <Text>Id Produto: {productData?.id}</Text>
                        <Text>Nome: {productData?.product_name}</Text>
                        <Text>
                            Preço: {productData?.currency?.symbol} {productData?.product_price}
                        </Text>
                        <Text>Estoque: {productData?.stock}</Text>
                        <Text>Categoria: {productData?.productCategory}</Text>
                        <Textarea value={productData?.product_description}></Textarea>

                        <Button
                            variant="filled"
                            color="yellow"
                            size="lg"
                            radius="lg">Comprar
                        </Button>
                    </Grid.Col>


                </Grid>
            </Paper>
        </Container>
    );
};

export default ProductDetail;
