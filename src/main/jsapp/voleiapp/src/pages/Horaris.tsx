import React from 'react';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import { DataGrid } from '@mui/x-data-grid';
import { useTheme, useMediaQuery } from '@mui/material';
import type { GridDataSource } from '@mui/x-data-grid';
import ListLoading from '../components/ListLoading';
import PartitDialog from '../components/PartitDialog';
import { localDate } from '../utils';

const CLUB_ID = 21;
/*const PARTITS_URL =
    'https://www.voleibolib.net/JSON/get_partidos_desglose_competiciones.asp?op={{op}}&fini=&ffin=';*/
const BACKEND_URL = '/api/partits?clubId=' + CLUB_ID + '&resultats={{resultats}}';

/*const directDataSource: (op: number) => GridDataSource = (op: number) => ({
    getRows: (_params) =>
        new Promise((resolve, reject) => {
            fetch(PARTITS_URL.replace('{{op}}', '' + op))
                .then((res) => res.json())
                .then((data) => {
                    const partits: any[] = [];
                    data.categorias.forEach((cat: any) => {
                        cat.competiciones.forEach((comp: any) => {
                            comp.fases.forEach((fase: any) => {
                                fase.grupos.forEach((grupo: any) => {
                                    grupo.partidos.forEach((partido: any) => {
                                        if (
                                            partido.ID_CLUB_LOCAL === CLUB_ID ||
                                            partido.ID_CLUB_VISITANTE === CLUB_ID
                                        ) {
                                            const [dia, mes, any] = partido.FECHA.split('/');
                                            const dataISO = `${any}-${mes}-${dia}T${partido.HORA}:00`;
                                            const resultat =
                                                partido.RESULTADO_LOCAL ||
                                                    partido.RESULTADO_VISITANTE
                                                    ? partido.RESULTADO_LOCAL +
                                                    ' - ' +
                                                    partido.RESULTADO_VISITANTE
                                                    : '...';
                                            partits.push({
                                                id: partido.ID,
                                                categoriaNom: cat.nombre,
                                                competicioNom: comp.nombre,
                                                faseNom: fase.nombre,
                                                grupId: grupo.id,
                                                grupNom: grupo.nombre,
                                                grupTorneigTipusId: grupo.id_tipo_torneo,
                                                grupTorneigTipusNom: grupo.tipo_torneo,
                                                clubLocalId: partido.ID_CLUB_LOCAL,
                                                clubVisitantId: partido.ID_CLUB_VISITANTE,
                                                data: new Date(dataISO),
                                                equipLocal: partido.ELOCAL,
                                                equipVisitant: partido.EVISITANTE,
                                                resultat,
                                                resultatLocal: partido.RESULTADO_LOCAL,
                                                resultatVisitant: partido.RESULTADO_VISITANTE,
                                                campNom: partido.Campo,
                                                campAdreca: partido.Direccion_Campo,
                                                municipi: partido.Municipio,
                                                arbitre: partido.arbitro1,
                                                torneigId: partido.TORNEO,
                                            });
                                        }
                                    });
                                });
                            });
                        });
                    });
                    partits.sort((a, b) => a.data - b.data);
                    resolve({
                        rows: partits,
                        rowCount: partits.length,
                    });
                })
                .catch(reject);
        }),
});*/

const backendDataSource: (op: number) => GridDataSource = (op: number) => ({
    getRows: (_params) =>
        new Promise((resolve, reject) => {
            const url = BACKEND_URL.replace('{{resultats}}', '' + (op === 2));
            fetch(url)
                .then((res) => res.json())
                .then((partits) => {
                    resolve({
                        rows: partits,
                        rowCount: partits.length,
                    });
                }).
                catch(reject);
        }),
});

export const useDataSource = (op: number) => {
    //return directDataSource(op);
    return backendDataSource(op);
};

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
        field: 'campNom',
        headerName: 'Lloc',
        flex: 0.8,
    },
];
const HorarisDataGrid: React.FC<{ dataSource: GridDataSource, onClick: (partit: any) => void }> = (props) => {
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

const HorarisList: React.FC<{ dataSource: GridDataSource, onClick: (partit: any) => void }> = (props) => {
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
                                sx={{ color: 'text.primary', fontWeight: p.equipLocalResaltat ? 'bold' : 'normal', mr: 1 }}
                            >
                                {p.equipLocal}
                            </Typography>
                            <Typography variant="subtitle2" sx={{ color: 'text.secondary', display: 'inline' }}>vs</Typography>
                            <Typography
                                component="span"
                                variant="body1"
                                sx={{ color: 'text.primary', fontWeight: p.equipVisitantResaltat ? 'bold' : 'normal', ml: 1 }}
                            >
                                {p.equipVisitant}
                            </Typography>
                        </>
                    }
                />
                <Box>
                    <Typography sx={{ textWrap: 'nowrap', pl: 2 }}>{localDate(p.data)}</Typography>
                </Box>
            </ListItem>
            {i < (partits.length - 1) && <Divider component="li" />}
        </>)}
    </List>;
};

export const Horaris = () => {
    const dataSource = useDataSource(1);
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
                <Typography variant="h4">Horaris propers partits</Typography>
            </Box>
            {isSmallScreen ?
                <HorarisList
                    dataSource={dataSource}
                    onClick={handlePartitClick} /> :
                <HorarisDataGrid
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

export default Horaris;
