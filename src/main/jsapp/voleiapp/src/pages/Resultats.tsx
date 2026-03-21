import React from 'react';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import { useTheme, useMediaQuery } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { useDataSource } from './Horaris';
import type { GridDataSource } from '@mui/x-data-grid';
import ListLoading from '../components/ListLoading';
import PartitDialog from '../components/PartitDialog';
import { localDate } from '../utils';

const columns = [
    {
        field: 'categoriaNom',
        headerName: 'Categoria',
        flex: 1,
    },
    {
        field: 'data',
        headerName: 'Data',
        flex: 0.8,
        valueFormatter: (value: any) => localDate(value),
    },
    {
        field: 'equipLocal',
        headerName: 'Local',
        flex: 1,
        renderCell: (params: any) => params.row.equipLocalResaltat ? <b>{params.value}</b> : params.value,
    },
    {
        field: 'equipVisitant',
        headerName: 'Visitant',
        flex: 1,
        renderCell: (params: any) => params.row.equipVisitantResaltat ? <b>{params.value}</b> : params.value,
    },
    {
        field: 'resultat',
        headerName: 'Resultat',
        flex: 0.4,
    },
];

export const ResultatsDataGrid: React.FC<{ dataSource: GridDataSource, onClick: (partit: any) => void }> = (props) => {
    const { dataSource, onClick } = props;
    return <Box>
        <DataGrid
            columns={columns}
            dataSource={dataSource}
            onDataSourceError={(error) => {
                console.log(error);
            }}
            initialState={{
                density: 'compact',
            }}
            disableColumnMenu
            disableColumnSorting
            onRowClick={(params) => onClick(params.row)}
            sx={{ '& .MuiDataGrid-footerContainer': { display: 'none' }, cursor: 'pointer' }}
        />
    </Box>;
};

const ResultatsList: React.FC<{ dataSource: GridDataSource, onClick: (partit: any) => void }> = (props) => {
    const { dataSource, onClick } = props;
    const [loading, setLoading] = React.useState<boolean>();
    const [partits, setPartits] = React.useState<any[]>();
    React.useEffect(() => {
        setLoading(true);
        dataSource.getRows(null as any).
            then((resultat) => {
                setPartits(resultat.rows);
            }).finally(() => setLoading(false));
    }, []);
    return loading ? <ListLoading /> : <List component={Paper} square elevation={1} sx={{ width: '100%', bgcolor: 'background.paper' }}>
        {partits?.map((p, i) => <>
            <ListItem alignItems="center" onClick={() => onClick(p)} sx={{ cursor: 'pointer' }}>
                <ListItemText
                    primary={<>
                        <Typography
                            component="span"
                            variant="body1"
                            sx={{ color: 'text.primary', display: 'inline', mr: 1 }}
                        >
                            {p.categoriaNom}
                        </Typography>
                    </>}
                    secondary={
                        <>
                            <Typography
                                component="span"
                                variant="body1"
                                sx={{ color: 'text.primary', display: 'inline', fontWeight: p.equipLocalResaltat ? 'bold' : 'normal', mr: 1 }}
                            >
                                {p.equipLocal}
                            </Typography>
                            <Typography variant="subtitle2" sx={{ color: 'text.secondary', display: 'inline' }}>vs</Typography>
                            <Typography
                                component="span"
                                variant="body1"
                                sx={{ color: 'text.primary', display: 'inline', fontWeight: p.equipVisitantResaltat ? 'bold' : 'normal', ml: 1 }}
                            >
                                {p.equipVisitant}
                            </Typography>
                        </>
                    }
                />
                <Box sx={{ px: 2 }}>
                    <Typography sx={{ color: 'text.secondary', textWrap: 'nowrap' }}>{localDate(p.data)}</Typography>
                </Box>
                <Box sx={{ width: '3em', textAlign: 'center' }}>
                    <Typography sx={{ textWrap: 'nowrap', fontWeight: 'bold' }}>{p.resultat}</Typography>
                </Box>
            </ListItem>
            {i < (partits.length - 1) && <Divider component="li" />}
        </>)}
    </List>;
}

export const Resultats = () => {
    const dataSource = useDataSource(2);
    const theme = useTheme();
    const isSmallScreen = useMediaQuery(theme.breakpoints.down('md'));
    const [dialogOpen, setDialogOpen] = React.useState<boolean>(false);
    const [currentPartit, setCurrentPartit] = React.useState<any>();
    const handlePartitClick = (partit: any) => {
        setCurrentPartit(partit);
        setDialogOpen(true);
    }
    return (
        <Stack spacing={2}>
            <Box>
                <Typography variant="h4">Darrers resultats</Typography>
            </Box>
            {isSmallScreen ?
                <ResultatsList
                    dataSource={dataSource}
                    onClick={handlePartitClick} /> :
                <ResultatsDataGrid
                    dataSource={dataSource}
                    onClick={handlePartitClick} />}
            <PartitDialog
                open={dialogOpen}
                partit={currentPartit}
                fullScreen={isSmallScreen}
                onClose={() => setDialogOpen(false)} />
        </Stack>
    );
};

export default Resultats;
