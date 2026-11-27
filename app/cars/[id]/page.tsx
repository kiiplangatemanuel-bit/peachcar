import { notFound } from "next/navigation";
import Link from "next/link";
import { ArrowLeft, ArrowUpRight, BadgeCheck, CarFront, Fuel, Gauge, Phone, MessageCircle } from "lucide-react";

type Vehicle={id:string;make:string;model:string;year:number;priceKes:number;mileageKm:number|null;fuel:string;transmission:string;bodyType:string;exteriorColour:string;description:string;images:{id:string;url:string;altText:string}[]};
type Settings={phoneNumber?:string;whatsappNumber?:string;contactEmail?:string};
const money=(n:number)=>"KES "+Number(n).toLocaleString("en-KE");
async function getJson<T>(path:string):Promise<T|null>{try{const base=process.env.API_BASE_URL||"http://localhost:8080";const r=await fetch(base+path,{cache:"no-store"});return r.ok?await r.json():null;}catch{return null;}}
export default async function VehicleDetail({params}:{params:Promise<{id:string}>}){
 const {id}=await params;
 const [vehicle,settings]=await Promise.all([getJson<Vehicle>("/api/public/vehicles/"+id),getJson<Settings>("/api/public/settings")]);
 if(!vehicle)notFound();
 const phone=(settings?.phoneNumber||"").trim();
 const wa=(settings?.whatsappNumber||"").replace(/\D/g,"");
 const waHref=wa?"https://wa.me/"+wa+"?text="+encodeURIComponent("Hello Peachcar, I am interested in the "+vehicle.year+" "+vehicle.make+" "+vehicle.model+"."):"#";
 return <main className="vehicle-detail-page"><div className="wrap detail-wrap"><Link href="/#cars" className="detail-back"><ArrowLeft size={16}/> Back to cars</Link><div className="detail-layout"><section><div className="detail-gallery">{vehicle.images?.length?vehicle.images.map((img)=><img key={img.id} src={img.url} alt={img.altText||vehicle.make+" "+vehicle.model}/>):<div className="detail-no-image"><CarFront size={48}/><span>Photos coming soon</span></div>}</div></section><aside className="detail-info"><span className="detail-kicker"><BadgeCheck size={14}/> PEACHCAR VEHICLE</span><p className="detail-year">{vehicle.year} · {vehicle.bodyType||"Vehicle"}</p><h1>{vehicle.make} {vehicle.model}</h1><strong className="detail-price">{money(vehicle.priceKes)}</strong><div className="detail-specs"><span><Gauge size={16}/>{vehicle.mileageKm==null?"Mileage on request":vehicle.mileageKm.toLocaleString()+" km"}</span><span><Fuel size={16}/>{vehicle.fuel||"Not specified"}</span><span><CarFront size={16}/>{vehicle.transmission||"Not specified"}</span></div><h2>About this vehicle</h2><p className="detail-description">{vehicle.description||"Contact our team for more information about this vehicle, its condition and viewing arrangements."}</p><div className="detail-contact"><p>Interested? Speak to our team.</p>{phone&&<a href={"tel:"+phone.replace(/\s/g,"")}><Phone size={16}/> Call {phone}</a>}{wa&&<a className="detail-whatsapp" href={waHref} target="_blank" rel="noreferrer"><MessageCircle size={16}/> Enquire on WhatsApp <ArrowUpRight size={15}/></a>}{!phone&&!wa&&<span>Contact details will be available shortly.</span>}</div></aside></div></div></main>;
}
