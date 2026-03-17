import React from 'react';
import Stack from '@mui/material/Stack';
import Skeleton from '@mui/material/Skeleton';

export const ListLoading: React.FC<{ rows?: number }> = (props) => {
    const { rows = 2 } = props;
    return <Stack spacing={1}>
        {new Array(rows).fill(null).map((_) => (<Skeleton variant="rectangular" height={60} />))}
    </Stack>;
}

export default ListLoading;
