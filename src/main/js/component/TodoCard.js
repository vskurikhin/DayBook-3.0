import {useGetOneTodoQuery, useUpdateTodoMutation, useRemoveTodoMutation} from '../redux/todoApi';

export function TodoItem(props) {
    const {data, isFetching, isSuccess} = useGetOneTodoQuery(props.id);

    const [updateTodo, {isLoading: isUpdating}] = useUpdateTodoMutation();
    const [removeTodo, {isLoading: isRemoving, isSuccess: isRemoved}] = useRemoveTodoMutation();

    const handleToggle = () => {
        updateTodo({
            id: props.id,
            value: data.value,
            visible: !data.visible,
        });
    };

    if (isRemoved) return null; // NEW если задача удалена
    if (isFetching) return <p className="info">Получение задачи {props.id} с сервера...</p>;
    if (!isSuccess) return <p className="error">Не удалось загрузить задачу {props.id}</p>;
    if (isUpdating) return <p className="info">Обновление задачи {props.id} на сервере...</p>;
    if (isRemoving) return <p className="info">Удаление задачи {props.id} на сервере</p>;

    console.log(JSON.stringify(data.value.map))

    return (
        <div className="todo-item">
            <span>
                <input type="checkbox" checked={data.visible} onChange={handleToggle}/>
                &nbsp;
                <span>{data.value.map.title}</span>
            </span>
            <span className="remove" onClick={() => removeTodo(props.id)}>
                &times;
            </span>
        </div>
    );
}
