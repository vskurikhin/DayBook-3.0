import {useState} from 'react';
import {useUpdateTodoMutation, useRemoveTodoMutation} from '../redux/todoApi';

export function TodoItem(props) {

    const [title, setTitle] = useState(props.item.value.map.title);
    const [visible, setVisible] = useState(props.item.visible);
    const [updateTodo, {isLoading: isUpdating}] = useUpdateTodoMutation();
    const [removeTodo, {isLoading: isRemoving, isSuccess: isRemoved}] = useRemoveTodoMutation();

    const handleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleToggle = () => {
        updateTodo({
            id: props.item.id,
            value: '{"title": "' + title + '"}',
            visible: visible,
        });
        setVisible(visible);
    };

    if (isRemoved) return null; // NEW если задача удалена
    if (isUpdating) return <p className="info">Обновление задачи {props.id} на сервере...</p>;
    if (isRemoving) return <p className="info">Удаление задачи {props.id} на сервере</p>;

    console.log(JSON.stringify(props.item.value.map))

    return (
        <div className="todo-form">
            <span>
                <input type="checkbox" checked={visible} onChange={handleToggle}/>
                &nbsp;
                <input type="text" value={title} onChange={handleChange} placeholder="Старая задача"/>
            </span>
            <span className="remove" onClick={() => removeTodo(props.item.id)}>
                &times;
            </span>
        </div>
    );
}
