//Assets
import Image from "next/image"



const Middleinfo: React.FC = () => {
  return (

    <div>
      <section className="mt-12 md:space-y-20">
        <div className="flex flex-col md:flex-row justify-center items-center space-x-4 md:space-x-52 ">
          <Image height={126} width={126} src="/persona3.png" alt="img1 middle" className="md:w-[226px] md:h-[226px]"></Image>
          <div className="flex flex-col justify-center items-center space-y-3 md:flex-row ">
            <div className="flex flex-col justify-center items-center space-y-3">
              <h2 className="text-xl text-middle-title md:text-4xl md:mr-28">Paquete de productos</h2>
              <p className="w-full max-w-md text-center md:text-left text-sm md:text-xl">Optimizá tus recursos, reducí costos y llevá tu negocio o emprendimiento al siguiente nivel. Descubrí soluciones eficientes que te ayudarán a maximizar tu rentabilidad y hacer crecer tu proyecto de manera inteligente.</p>
            </div>
            <button className="w-[290px] h-[55px] bg-middle-title rounded-full text-white hover:bg-green-hover">Conocer mas ⭢</button>
          </div>

        </div>

        <div className="flex flex-col md:flex-row justify-center items-center space-x-4 md:space-x-52 mt-9">
          <Image height={126} width={126} src="/persona4.png" alt="img1 middle" className="md:w-[226px] md:h-[226px]"></Image>
          <div className="flex flex-col justify-center items-center space-y-3 md:flex-row ">
            <div className="flex flex-col justify-center items-center space-y-3">
              <h2 className="text-xl text-middle-title md:text-4xl md:mr-28">¿Cómo evitar estafas?</h2>
              <p className="w-full max-w-md text-center md:text-left text-sm md:text-xl">Optimizá tus recursos, reducí costos y llevá tu negocio o emprendimiento al siguiente nivel. Descubrí soluciones eficientes que te ayudarán a maximizar tu rentabilidad y hacer crecer tu proyecto de manera inteligente.</p>
            </div>
            <button className="w-[290px] h-[55px] bg-middle-title rounded-full text-white hover:bg-green-hover">Conocer mas ⭢</button>
          </div>

        </div>

      </section>
    </div>
  )
}

export default Middleinfo