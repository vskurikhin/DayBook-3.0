import {Link, useParams} from 'react-router-dom';
import WordService from '../../services/WordService';

const SinglePost = () => {
    const {productId} = useParams();
    const wordService = new WordService();
    const product = wordService.find(productId);
    // const product = products.find((product) => product.id === productId);
    const {image, name} = product;
    return (
        <section className='section product'>
            <img src={image} alt={name}/>
            <h5>{name}</h5>
            <Link to='/products'>back to products</Link>
        </section>
    );
};

export default SinglePost;
