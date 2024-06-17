
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from '../components/Home';
import Pagina404 from '../components/Pagina404';

function RoutesService() {

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/*" element={<Pagina404 />} />
        </Routes>
      </BrowserRouter>
    </>
  )

}
export default RoutesService;