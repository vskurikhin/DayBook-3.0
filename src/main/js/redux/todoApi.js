import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';

export const todoApi = createApi({
    reducerPath: 'todo',
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/v1'
    }),
    tagTypes: ['todo'],
    endpoints: (builder) => ({
        getAllTodo: builder.query({
            query: () => '/key-values',
            providesTags: [{type: 'todo', id: 'list'}],
        }),
        getOneTodo: builder.query({
            query: (id) => `/key-value/${id}`,
            providesTags: (res, err, arg) => [{type: 'todo', id: arg}],
        }),
        createTodo: builder.mutation({
            query: (data) => ({
                url: '/key-value',
                method: 'POST',
                body: data,
            }),
            invalidatesTags: [{type: 'todo', id: 'list'}],
        }),
        updateTodo: builder.mutation({
            query: (data) => {
                return {
                    url: `/key-value`,
                    method: 'PUT',
                    body: data,
                };
            },
            invalidatesTags: (res, err, arg) => [{type: 'todo', id: arg.id}],
        }),
        removeTodo: builder.mutation({
            query: (id) => ({
                url: `/key-value/${id}`,
                method: 'DELETE',
            }),
            // NEW при удалении задачи не надо ничего запрашивать с сервера
        }),
    }),
});

export const {
    useGetAllTodoQuery,
    useGetOneTodoQuery,
    useCreateTodoMutation,
    useUpdateTodoMutation,
    useRemoveTodoMutation,
} = todoApi;
