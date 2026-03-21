export const abreujar = (text: string) => {
    if (!text) return text;
    return text
        .split(' ')
        .map((p) => (p.length > 3 ? p.slice(0, 3) + '.' : p))
        .join(' ');
};

export const dataAmbFormat = (data: Date) => {
    if (data != null) {
        return data.toLocaleDateString('ca-ES', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
        }) +
            ' ' +
            data.toLocaleTimeString('ca-ES', { hour: '2-digit', minute: '2-digit' });
    } else {
        return data;
    }
}

export const localDate = (iso: string) => {
    return dataAmbFormat(new Date(iso));
}