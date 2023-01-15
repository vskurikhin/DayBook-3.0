import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const todoApi = createApi({
    reducerPath: 'todo',
    baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:8080/api' }),
    tagTypes: ['todo'],
    endpoints: (builder) => ({
        getAllTodo: builder.query({
            query: () => '/v1/key-values',
            providesTags: [{ type: 'todo', id: 'list' }],
        }),
        getOneTodo: builder.query({
            query: (id) => `/v1/key-value/${id}`,
            providesTags: (res, err, arg) => [{ type: 'todo', id: arg }],
        }),
        createTodo: builder.mutation({
            query: (data) => ({
                url: '/v1/key-value',
                method: 'POST',
                body: data,
            }),
            invalidatesTags: [{ type: 'todo', id: 'list' }],
        }),
        updateTodo: builder.mutation({
            query: (data) => {
                return {
                    url: `/v1/key-value`,
                    method: 'PUT',
                    body: data,
                };
            },
            invalidatesTags: (res, err, arg) => [{ type: 'todo', id: arg.id }],
        }),
        removeTodo: builder.mutation({
            query: (id) => ({
                url: `/v1/key-value/${id}`,
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
