import { ITransfer } from "@/src/types/ITransfer";
import axios from "axios";
import { useState, } from "react";
//Libraries
import Swal from "sweetalert2";
//Context
import { useWebApp } from "@/src/context/WebappContext";

interface NewTransferProps {
    goBack: () => void;
}

const NewTransfer: React.FC<NewTransferProps> = ({ goBack }) => {
    const BACKEND_URL = process.env.NEXT_PUBLIC_BACKEND_URL;
    const {accessToken} = useWebApp();
    const [errorAmountVacio,setErrorAmountVacio] = useState<string>("");
    const [errorCuentaVacio,setErrorCuentaVacio] = useState<string>("");

    

    const [dataForm, setDataForm] = useState<ITransfer>({
        receiverAccountNumber: "",
        amount: 0,
        transactionDate: new Date().toISOString(),
    });

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setDataForm((prevState) => ({
            ...prevState,
            [name]: value
        }))
    }

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if(!dataForm.amount ){
            setErrorAmountVacio("❌Debes de poner un monto mayor a 0 a transferir");
        }
        if(!dataForm.receiverAccountNumber){
            setErrorCuentaVacio("❌Debes de poner un numero de cuenta al cual transferir");
        }
        try {
            const response = await axios.post(`${BACKEND_URL}/api/banca/transacciones/transferir`, dataForm,{
                headers:{
                    Authorization:`Bearer ${accessToken}`,
                    "Content-Type": "application/json"
                }
            });
            console.log("TRANSFERENCIA REALIZADA CON EXITO",response.data);
               

            Swal.fire({
                title: "Transferencia enviada con exito",
                icon: "success",
                draggable: true
            });
            setDataForm({
                receiverAccountNumber: "",
                amount: 0,
                transactionDate: new Date().toISOString(),
            })

        } catch (error) {
            console.log("ERROR AL TRANSFERIR", error)
        }
    }

    return (
        <div className="flex flex-col items-center">
            <div className="flex flex-row justify-between md:px-10 md:space-x-80 py-6 space-x-10">
                <p className="text-[22px] text-nowrap 2xl:mr-[600px]">Saldo Disponible</p>
                <p className="text-[20px]"></p>
            </div>

            <div className="flex md:flex-row  flex-col justify-center items-center bg-transfer-bg md:w-[676px] md:h-[418px] w-full h-auto rounded-[20px] space-x-6 2xl:mr-[600px] ">
                <div className="flex flex-col justify-center items-center">
                    <p className="text-transfer-color text-[28px]">Transferencia</p>
                    <form action="" className="flex flex-col justify-center items-center space-y-4 " onSubmit={handleSubmit}>
                        <input type="text" name="receiverAccountNumber" placeholder={"CBU"} value={dataForm.receiverAccountNumber} className="md:w-[312px] md:h-[44px]  border-[1px] border-transfer-inputs rounded-[8px] placeholder:text-black" onChange={handleChange} />
                        {errorCuentaVacio && <p className="text-red-500 text-sm mt-1 text-center">{errorCuentaVacio}</p>}

                        <input type="number" name="amount" placeholder="MONTO" value={dataForm.amount || ""} className="md:w-[312px] md:h-[44px] border-[1px] border-transfer-inputs rounded-[8px] placeholder:text-black" onChange={handleChange} />
                        {errorAmountVacio && <p className="text-red-500 text-sm mt-1 text-center">{errorAmountVacio}</p>}
                        <input type="text" name="transactionDate" readOnly value={dataForm.transactionDate} className="hidden md:w-[312px] md:h-[44px] border-[1px] border-transfer-inputs rounded-[8px] placeholder:text-black" />

                        <button className="flex flex-col justify-center items-center bg-nav-buttons rounded-[30px] w-[125px] h-[45px] hover:bg-buttons-hover hover:text-white">Enviar Dinero</button>
                    </form>
                </div>
                <img src="/LOGO.png" alt="" />
            </div>
            <button onClick={goBack} className="bg-nav-buttons rounded-[30px] w-[125px] h-[48px] hover:bg-buttons-hover hover:text-white mt-5 2xl:mr-[600px]">Mis Transacciones</button>

        </div>
    )
}

export default NewTransfer