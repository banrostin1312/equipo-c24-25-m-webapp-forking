"use client"
//Libraries
import Swal from "sweetalert2"
//Interfaces
import { IRegister } from "@/src/types/IRegister"
//Assets
import { useState } from "react"
import axios from "axios"
import { useRouter } from "next/navigation"
//Context
const BACKEND_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

const Registrarse: React.FC = () => {
const router = useRouter();

    const [dataForm, setDataForm] = useState<IRegister>({
        "name": "",
        "age": 0,
        "email": "",
        "password": "",
        "country": "",
        "dni": ""
    })
    const [emaileErrorMessage,setEmailErrorMessage] = useState<string>("");
    const [passwordErrorMessage,setPasswordErrorMessage] = useState<string>("");


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setDataForm((prevState) => ({
            ...prevState,
            [name]: value,
        }))

        if(name === "email"){
            if (/^\S+@\S+\.\S+$/.test(value)) {
                setEmailErrorMessage("");  // Si el correo es válido, borra el mensaje de error
            }
        }

        if(name === "password"){
            if(!/^(?=.*[A-Z])(?=.*[\W_]).{8,}$/.test(value))
                setPasswordErrorMessage("");
        }
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!/^\S+@\S+\.\S+$/.test(dataForm.email)) {
            setEmailErrorMessage("❌ Ingresa un correo válido");
            return;
        }

        if (!/^(?=.*[A-Z])(?=.*[\W_]).{8,}$/.test(dataForm.password)) {
            setPasswordErrorMessage("❌ La contraseña debe tener al menos 8 caracteres, una mayúscula y un carácter especial");
            return;
        }

        try {
            
            const response = await axios.post(`${BACKEND_URL}/api/banca/auth/registrarse`, dataForm)
         console.log("registro existoso",response.data)

          
            Swal.fire({
                title: "Registro Exitoso",
                icon: "success",
                draggable: true
              });
           

            setDataForm({
                "name": "",
                "age": 0,
                "email": "",
                "password": "",
                "country": "",
                "dni": ""
            })
              router.push("/iniciarSesion");
        } catch (error) {
            console.error("ERROR AL REGISTRARSE", error);
            alert("HUBO UN ERROR AL REGISTRARSE")
        }
    };

    return (
        <div className="flex flex-col items-center justify-center space-y-6 md:w-[457px] md:h-[656px] md:max-w-[456px] md:max-h-[656px]">
            <h3 className="text-4xl">Registrate</h3>
            <div className="flex flex-col items-center justify-center md:w-[457px] md:h-[570px] border-[1px] border-black">

                <div className="flex justify-center items-center bg-middle-title md:w-[457px] w-[375px] md:max-w-[457px] h-[60px] "><img src="/LOGOsinbanca.png" alt="Logo" className="w-[166px] h-[60px]" /></div>
                <form action="" className="flex flex-col justify-center items-center md:w-[343px] md:h-[500px] border-[1px] border-border-forms rounded-[20px] mt-2 mb-2" onSubmit={handleSubmit}>
                    <label htmlFor="">Nombre:</label>
                    <input  value={dataForm.name} onChange={handleChange} type="text" name="name" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md text-center" />

                    <label htmlFor="">Edad:</label>
                    <input value={dataForm.age} onChange={handleChange} type="number" name="age" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md  text-center" />

                    <label htmlFor="">Email:</label>
                    <input value={dataForm.email} onChange={handleChange} type="text" name="email" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md text-center" />
                      {emaileErrorMessage && <p className="text-red-500 text-sm mt-1 text-center">{emaileErrorMessage}</p>}
                    <label htmlFor="">Contraseña:</label>
                    <input value={dataForm.password} onChange={handleChange} type="password" name="password" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md text-center" />
                    {passwordErrorMessage && <p className="text-red-500 text-sm mt-1 text-center">{passwordErrorMessage}</p>}

                    <label htmlFor="">Pais:</label>
                    <input value={dataForm.country} onChange={handleChange} type="text" name="country" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md text-center" />

                    <label htmlFor="">DNI:</label>
                    <input value={dataForm.dni} onChange={handleChange} type="text" name="dni" id="" className="w-[234px] h-[39px] border-2 border-input-border rounded-md text-center" />

                    <button type="submit" className="w-[125px] h-[45px] bg-nav-buttons flex justify-center items-center rounded-[30px] mt-8 hover:bg-buttons-hover hover:text-white"><p className="text-[11px]">Registrate</p></button>
                </form>
            </div>
        </div>
    )
}

export default Registrarse