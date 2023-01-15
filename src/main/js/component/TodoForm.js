import {useState} from 'react';
import {useCreateTodoMutation} from '../redux/todoApi.js';

export function TodoForm(props) {
    const [text, setText] = useState('');
    const [createTodo] = useCreateTodoMutation();

    const handleChange = (event) => {
        setText(event.target.value);
    };

    const handleClick = () => {
        if (text.trim().length !== 0) {
            const data = {
                value: '{"title": "' + text.trim() + '"}',
                visible: true,
            };
            createTodo(data);
        }
        setText('');
    };

    return (
        <div className="todo-form">
            <input type="text" value={text} onChange={handleChange} placeholder="Новая задача"/>
            <button onClick={handleClick}>Добавить</button>
        </div>
    );
}
