import DataTableLazyDemo from './DataTableLazyDemo';
import PostsTableLazy from "./PostsTableLazy";

const Posts = () => {
    return (
        <section className='section'>
            <div className='products'>
                <PostsTableLazy/>
            </div>
        </section>
    );
};

export default Posts;
