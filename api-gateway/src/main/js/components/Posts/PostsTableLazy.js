import React, {useState, useEffect} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import VocabularyService from '../../services/VocabularyService';

const PostsTableLazy = () => {

    const [customers, setCustomers] = useState(null);
    const [loading, setLoading] = useState(false);
    const [selectAll, setSelectAll] = useState(false);
    const [selectedCustomers, setSelectedCustomers] = useState(null);
    const [totalRecords, setTotalRecords] = useState(0);
    const [lazyParams, setLazyParams] = useState({
        first: 0,
        rows: 10,
        page: 0,
        sortField: null,
        sortOrder: null,
        filters: {
            'word': { value: '', matchMode: 'contains' },
            'visible': { value: '', matchMode: 'contains' },
            'flags': { value: '', matchMode: 'contains' },
        }
    });

    const wordService = new VocabularyService();

    let loadLazyTimeout = null;

    useEffect(() => {
        loadLazyData();
    }, [lazyParams]) // eslint-disable-line react-hooks/exhaustive-deps

    const loadLazyData = () => {
        setLoading(true);

        if (loadLazyTimeout) {
            clearTimeout(loadLazyTimeout);
        }

        //imitate delay of a backend call
        loadLazyTimeout = setTimeout(() => {
            wordService.getCustomers(lazyParams).then(data => {
                setTotalRecords(data.totalRecords);
                setCustomers(data.content);
                setLoading(false);
            });
        }, Math.random() * 3 + 1);
    }

    const onPage = (event) => {
        setLazyParams(event);
    }

    const onSort = (event) => {
        setLazyParams(event);
    }

    const onFilter = (event) => {
        event['first'] = 0;
        setLazyParams(event);
    }

    const onSelectionChange = (event) => {
        const value = event.value;
        setSelectedCustomers(value);
        setSelectAll(value.length === totalRecords);
    }

    const onSelectAllChange = (event) => {
        const selectAll = event.checked;

        if (selectAll) {
            customerService.getCustomers().then(data => {
                setSelectAll(true);
                setSelectedCustomers(data.content);
            });
        }
        else {
            setSelectAll(false);
            setSelectedCustomers([]);
        }
    }

    const wordBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                {/*<img alt={rowData.representative.name} src={`images/avatar/${rowData.representative.image}`}*/}
                {/*     onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'}*/}
                {/*     width={32} style={{verticalAlign: 'middle'}}/>*/}
                <span className="image-text">{rowData.word}</span>
            </React.Fragment>
        );
    }

    const visibleBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                {/*<img alt="flag" src="/images/flag/flag_placeholder.png"*/}
                {/*     onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'}*/}
                {/*     className={`flag flag-${rowData.country.code}`} width={30}/>*/}
                <span className="image-text">{rowData.visible ? 'True' : 'False'}</span>
            </React.Fragment>
        );
    }

    return (
        <div>
            <div className="card">
                <DataTable value={customers} lazy filterDisplay="row" responsiveLayout="scroll" dataKey="id"
                           paginator first={lazyParams.first} rows={10} totalRecords={totalRecords} onPage={onPage}
                           onSort={onSort} sortField={lazyParams.sortField} sortOrder={lazyParams.sortOrder}
                           onFilter={onFilter} filters={lazyParams.filters} loading={loading}
                           selection={selectedCustomers} onSelectionChange={onSelectionChange}
                           selectAll={selectAll} onSelectAllChange={onSelectAllChange}>
                    <Column selectionMode="multiple" headerStyle={{ width: '3em' }}></Column>
                    <Column field="word" header="Word" sortable body={wordBodyTemplate} filter filterPlaceholder="Search by word" />
                    <Column field="visible" sortable header="Visible" body={visibleBodyTemplate} filter filterPlaceholder="Search by visible" />
                    <Column field="flags" sortable filter header="Flags" filterPlaceholder="Search by flags" />
                </DataTable>
            </div>
        </div>
    );
}

export default PostsTableLazy;
