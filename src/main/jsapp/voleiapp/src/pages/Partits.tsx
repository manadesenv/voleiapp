import React from 'react';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import CircularProgress from '@mui/material/CircularProgress';

const CLUB_ID = 21;

const localDate = (iso: string) => {
    const data = new Date(iso);
    return data.toLocaleDateString([], {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    }) + ' ' +
        data.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit'
        });
}

export const Partits = () => {
    const [loading, setLoading] = React.useState<boolean>(false);
    const [partits, setPartits] = React.useState<any[]>();
    React.useEffect(() => {
        setLoading(true);
        const queryString = new URLSearchParams({ clubId: CLUB_ID, tipus: 'LLIGA' } as any).toString();
        fetch("/api/partits?" + queryString).
            then(res => res.json()).
            then(setPartits).
            catch(err => console.error(err)).
            finally(() => setLoading(false));
    }, []);
    return (
        <Stack spacing={2}>
            <Box style={{ display: 'flex', alignItems: 'flex-end' }}>
                <img src={'/api/logo?clubId=' + CLUB_ID} width={80} />
                <Typography variant="h3" sx={{ ml: 2 }}>Propers partits</Typography>
                {loading && <>
                    <Box style={{ flexGrow: 1 }} />
                    <CircularProgress size={60} sx={{ mr: 2 }} />
                </>}
            </Box>
            {!loading && <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Categoria</TableCell>
                            <TableCell>Data</TableCell>
                            <TableCell>Local</TableCell>
                            <TableCell>Visitant</TableCell>
                            <TableCell>Resultat</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {partits?.map((partit, i) => (
                            <TableRow
                                key={i}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                            >
                                <TableCell component="th" scope="row">
                                    {partit.competicio.nom + (partit.competicio.grup ? ', ' + partit.competicio.grup : '')}
                                </TableCell>
                                <TableCell>{localDate(partit.data)}</TableCell>
                                <TableCell>{partit.local.nom}</TableCell>
                                <TableCell>{partit.visitant.nom}</TableCell>
                                <TableCell>{partit.puntuacioLocal != null && partit.puntuacioVisitant != null ? partit.puntuacioLocal + ' - ' + partit.puntuacioVisitant : '...'}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>}
        </Stack>
    );
}

export default Partits;