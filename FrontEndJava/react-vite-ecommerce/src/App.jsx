import '@mantine/core/styles.css';
import './App.css'
import {createTheme, MantineProvider} from "@mantine/core";
import RouteService from "./routes/routeService.jsx";
import Footer from "./components/footer/footer.jsx";
import AppHeader from "./components/header/AppHeader.jsx";

const myTheme = {
    colors: {
        // Defina suas próprias cores aqui
        primary: ['#e0f7fa', '#b2ebf2', '#80deea', '#4dd0e1', '#26c6da', '#00bcd4', '#00acc1', '#0097a7', '#00838f', '#006064'],
        second: [      '#f3e5f5', '#e1bee7', '#ce93d8', '#ba68c8', '#ab47bc', '#9c27b0', '#8e24aa', '#7b1fa2', '#6a1b9a', '#4a148c'],
        yellow2: [      '#ffffe0', '#fffacd', '#fff8dc', '#ffeb3b', '#ffd700', '#ffc107', '#ffb300', '#ffa000', '#ff8f00', '#ff6f00'],

    },

    primaryColor: 'primary', // Defina a cor primária do seu tema
    secondaryColor: 'secondary',
};


function App() {

    return (
        <MantineProvider theme={myTheme}>
            {/* Your app here */}
            <AppHeader/>
            <RouteService/>
            <Footer/>
        </MantineProvider>

    )
}

export default App
