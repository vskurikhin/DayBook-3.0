import {TodoItem} from './TodoItem';
import {actions} from "../redux/actions";
import {selectors} from "../redux/selectors";
import {useGetAllTodoQuery} from '../redux/todoApi';

import {useDispatch, useSelector} from "react-redux";

export function TodoList(props) {
    const {data, isFetching, isSuccess} = useGetAllTodoQuery(null);

    // извлекаем их хранилища данные по авторизации (да,нет)
    const first = useSelector(selectors.user.first);
    console.log('first')
    console.log(first)

    // создаем две функции для отправки экшенов в хранилище
    const dispatch = useDispatch();
    const login = () => dispatch(actions.user.login());
    const logout = () => dispatch(actions.user.logout());

    if (isFetching) return <p className="info">Получение списка задач с сервера...</p>;
    if (!isSuccess) return <p className="error">Не удалось загрузить список</p>;

    return (
        <>
            <div className="todo-list">
                {data.length > 0 ? (
                    data.map((item) => <TodoItem key={item.id} item={item}/>)
                ) : (
                    <p>Список задач пустой</p>
                )}
            </div>
            <div className="user-login">
                {first.auth ? (
                    <>
                        <span>Пользователь авторизован</span>
                        &nbsp;
                        <button onClick={logout}>Выйти</button>
                    </>
                ) : (
                    <>
                        <span>Пользователь не авторизован</span>
                        &nbsp;
                        <button onClick={login}>Войти</button>
                    </>
                )}
            </div>
        </>
    );
}
