import React from 'react';
import { Card, CardBody, CardHeader, CardFooter, CardTitle, CardText,Container} from 'reactstrap';
import './vend.css';

class Product extends React.Component
{

    ProductRow( start,  end)
    {
        let result = [];
        for (var i=start; i<=end;i++)
        {
            var statusClass;
            if(this.props.productData[i][5]>=7){
                statusClass = "green";
            } else if(this.props.productData[i][5]<=3){
                statusClass = "grey";
            } else{
                statusClass = "yellow";
            }
            result.push(
            <Card key={i} className={"product " + statusClass}>
               <CardBody>
                <CardTitle>{this.props.productData[i][0] + "(" + this.props.productData[i][5] + ")"}</CardTitle>
                <CardText>{this.props.productData[i][1]}</CardText>
               </CardBody> 
               <CardFooter>{this.props.productData[i][4]}</CardFooter>
            </Card>
            )
        }
        return result;
    }

    render()
    {
        return(
            <Container className='mt-1 flex-container centered border3 rounded mx-auto' >
                
                {this.ProductRow(0,3)}

                {this.ProductRow(4,7)}

        </Container>

        )
    }
}

export default Product;