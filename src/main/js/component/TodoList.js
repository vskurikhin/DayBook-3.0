import { useGetAllTodoQuery } from '../redux/todoApi.js';
import { TodoItem } from './TodoItem.js';

export function TodoList(props) {
    const { data, isFetching, isSuccess } = useGetAllTodoQuery(null);

    if (isFetching) return <p className="info">Получение списка задач с сервера...</p>;
    if (!isSuccess) return <p className="error">Не удалось загрузить список</p>;

    return (
        <>
            <div className="todo-list">
                {data.length > 0 ? (
                    data.map((item) => <TodoItem key={item.id} id={item.id} />)
                ) : (
                    <p>Список задач пустой</p>
                )}
            </div>
        </>
    );
}
