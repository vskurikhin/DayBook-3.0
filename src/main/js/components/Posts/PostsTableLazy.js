import React, {useState, useEffect} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import WordService from '../../services/WordService';

const PostsTableLazy = () => {

    const [loading, setLoading] = useState(false);
    const [totalRecords, setTotalRecords] = useState(0);
    const [customers, setCustomers] = useState(null);
    const [lazyParams, setLazyParams] = useState({
        first: 0,
        rows: 5,
        page: 0,
        sortField: null,
        sortOrder: null,
    });

    const wordService = new WordService();

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
                console.log('data')
                console.log(data)
                setTotalRecords(data.totalRecords);
                setCustomers(data.content);
                setLoading(false);
            });
        }, Math.random() * 1000 + 250);
    }

    const onPage = (event) => {
        console.log('onPage')
        console.log(event)
        setLazyParams(event);
    }

    const wordBodyTemplate = (rowData) => {
        console.log('wordBodyTemplate rowData')
        console.log(rowData)
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
        console.log('visibleBodyTemplate rowData')
        console.log(rowData)
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
                <DataTable value={customers} lazy filterDisplay="row" responsiveLayout="scroll" dataKey="word"
                           paginator first={lazyParams.first} rows={5} totalRecords={totalRecords} onPage={onPage}
                           loading={loading}>
                    <Column field="word" header="Word" sortable body={wordBodyTemplate}/>
                    <Column field="visible" header="Visible" body={visibleBodyTemplate}/>
                    <Column field="flags" header="Flags"/>
                </DataTable>
            </div>
        </div>
    );
}

export default PostsTableLazy;
