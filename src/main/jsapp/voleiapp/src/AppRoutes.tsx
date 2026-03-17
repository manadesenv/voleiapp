import { Routes, Route, Navigate } from 'react-router-dom';
import Horaris from './pages/Horaris';
import Resultats from './pages/Resultats';
import NotFound from './pages/NotFound';

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/horaris" replace />} />
            <Route path="/horaris" element={<Horaris />} />
            <Route path="/resultats" element={<Resultats />} />
            <Route path="*" element={<NotFound />} />
        </Routes>
    );
}

export default AppRoutes;
