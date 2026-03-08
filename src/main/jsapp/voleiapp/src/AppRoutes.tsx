import { Routes, Route, Navigate } from 'react-router-dom';
import Partits from './pages/Partits';
import NotFound from './pages/NotFound';

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/partits" replace />} />
            <Route path="/partits" element={<Partits />} />
            <Route path="*" element={<NotFound />} />
        </Routes>
    );
}

export default AppRoutes;
