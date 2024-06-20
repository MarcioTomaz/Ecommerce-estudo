import '@mantine/core/styles.css';
import './App.css'
import {createTheme, MantineProvider} from "@mantine/core";
import RouteService from "./routes/routeService.jsx";

const theme = createTheme({
    /** Put your mantine theme override here */
});


function App() {

  return (
      <MantineProvider theme={theme}>
          {/* Your app here */}
          <RouteService/>
      </MantineProvider>

  )
}

export default App
