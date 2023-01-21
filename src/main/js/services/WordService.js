class WordService {

    find(id) {
        return fetch('/api/v1/word/' + id)
            .then(res => res.json());
    }

    getCustomers(params) {
        console.log('params')
        console.log(params)
        const {rows, page} = params;
        const limit = rows ? rows : 0;
        return fetch('/api/v1/word/-?page=' + (page ? page : 1) + '&limit=' + limit)
            .then(res => res.json());
    }
}

export default WordService;