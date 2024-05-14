class VocabularyService {

    find(id) {
        return fetch('/api/v1/vocabulary/' + id)
            .then(res => res.json());
    }

    getCustomers(params) {
        console.log('params')
        console.log(params)
        const {filters, rows, page, sortField, sortOrder} = params;
        const limit = rows ? rows : 0;
        return fetch('/api/v1/vocabulary/-?page=' + (page ? page : 0) + '&limit=' + limit)
            .then(res => res.json());
    }
}

export default VocabularyService;