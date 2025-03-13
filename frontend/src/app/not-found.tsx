import Link from 'next/link'
import Image from 'next/image'
import './404.css'

const notfound: React.FC = () => {
  return (
    <>
      <Image
        width={280}
        height={200}
        className="vector"
        src="/vector.svg"
        alt="404"
      />
      <section className="flex flex-col md:flex-row justify-center items-center space-y-6 md:space-y-0 mt-4 md:mt-0 mb-4 md:mb-0">
        <div className="z-10">
          <h3 className="text-orange-500 text-[10em] font-bold">404</h3>
          <h4 className="text-[2em] mb-4">OOOps! Esta página no funciona</h4>
          <p className="text-gray-500 mb-2">
            ¡Esta página no existe o fue eliminada!
          </p>
          <p className="text-gray-500 mb-6">
            Te sugerimos volver a la página de inicio
          </p>
          <Link href="/">
            <button className="bg-nav-buttons hover:bg-buttons-hover p-3 rounded-full w-[125px] h-[45px] flex justify-center items-center hover:text-white active:shadow-[0px_30px_10px_-20px_#119E1F] transition-colors duration-300">
              Volver a inicio
            </button>
          </Link>
        </div>
        <div className="flex flex-col md:flex-row justify-center items-center space-y-6 md:space-y-0 mt-4 md:mt-0 mb-4 md:mb-0">
          <Image width={600} height={200} src="/404.svg" alt="404" />
        </div>
      </section>
    </>
  )
}

export default notfound
