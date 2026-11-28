"use client";

import { useEffect, useMemo, useState } from "react";
import {
  ArrowRight, ArrowUpRight, BadgeCheck, CalendarDays, CarFront, CheckCircle2,
  ChevronDown, ChevronLeft, ChevronRight, CircleHelp, Fuel, Gauge, Heart,
  Mail, MapPin, Menu, MessageCircle, Phone, Search, ShieldCheck, SlidersHorizontal,
  Wallet, X, Users, Sparkles
} from "lucide-react";

type Vehicle = {
  id:string; make:string; model:string; year:number; priceKes:number;
  mileageKm:number|null; fuel:string|null; transmission:string|null;
  bodyType:string|null; exteriorColour:string|null; description:string|null;
  images:{id:string;url:string;altText:string;sortOrder:number}[];
  createdAt?:string;
};
type Settings = {phoneNumber?:string|null;whatsappNumber?:string|null;contactEmail?:string|null};
type Filters = {make:string;body:string;fuel:string;transmission:string;yearFrom:string;yearTo:string;priceFrom:string;priceTo:string;mileageTo:string};
const initialFilters:Filters={make:"",body:"",fuel:"",transmission:"",yearFrom:"",yearTo:"",priceFrom:"",priceTo:"",mileageTo:""};
const money=(n:number)=>"KSh "+Number(n||0).toLocaleString("en-KE");
const digits=(s:string)=>s.replace(/\\D/g,"");

function Brand(){return <a className="market-brand" href="/" aria-label="Peachcar home"><span className="market-brand-mark">p</span><span>peachcar<span className="market-brand-dot">.</span></span></a>}

function Header({phone,whatsapp}:{phone:string;whatsapp:string}){
 const [menuOpen,setMenuOpen]=useState(false);
 return <>
  <div className="market-topbar"><div className="market-shell market-topbar-inner"><div><a href="#buyers">Peachcar for Buyers</a><i/><a href="#sellers">Peachcar for Sellers</a><i/><a href="#services">Our Services <ChevronDown size={12}/></a></div><div className="market-top-contact">{phone&&<a href={"tel:"+phone.replace(/[^\\d+]/g,"")}><Phone size={13}/> Call: {phone}</a>}{whatsapp&&<a href={"https://wa.me/"+whatsapp} target="_blank" rel="noreferrer"><MessageCircle size={13}/> WhatsApp</a>}<a href="/admin"><Users size={13}/> Admin sign in</a></div></div></div>
  <header className="market-header"><div className="market-shell market-nav"><Brand/><button className="market-menu" onClick={()=>setMenuOpen(!menuOpen)} aria-label="Toggle navigation">{menuOpen?<X/>:<Menu/>}</button><nav className={menuOpen?"market-nav-links open":"market-nav-links"}><a href="#inventory" onClick={()=>setMenuOpen(false)}>Find a Car</a><a href="#sellers" onClick={()=>setMenuOpen(false)}>Sell a Car</a><a href="#services" onClick={()=>setMenuOpen(false)}>Our Services</a><a href="#buyers" onClick={()=>setMenuOpen(false)}>How it Works</a><a href="#contact" onClick={()=>setMenuOpen(false)}>Contact</a></nav><a className="market-signin" href="/admin"> <Users size={16}/> Sign in <ArrowUpRight size={14}/></a></div></header>
 </>;
}

function VehicleCard({vehicle,whatsapp}:{vehicle:Vehicle;whatsapp:string}){
 const image=vehicle.images?.[0]?.url||"/car-placeholder.svg";
 const message=encodeURIComponent("Hello Peachcar, I am interested in the "+vehicle.year+" "+vehicle.make+" "+vehicle.model+".");
 const enquiry=whatsapp?"https://wa.me/"+whatsapp+"?text="+message:"#contact";
 return <article className="market-car-card">
  <a className="market-car-image" href={"/cars/"+vehicle.id}><img src={image} alt={vehicle.images?.[0]?.altText||vehicle.make+" "+vehicle.model}/><span className="market-car-badge"><BadgeCheck size={13}/> Peachcar listing</span></a>
  <div className="market-car-content"><div className="market-car-meta">{vehicle.year} <span>·</span> {vehicle.bodyType||"Vehicle"} <span>·</span> {vehicle.exteriorColour||"Colour on request"}</div><h3><a href={"/cars/"+vehicle.id}>{vehicle.make} {vehicle.model}</a></h3><div className="market-car-specs"><span><Gauge size={14}/>{vehicle.mileageKm==null?"Mileage on request":Number(vehicle.mileageKm).toLocaleString()+" km"}</span><span><Fuel size={14}/>{vehicle.fuel||"Fuel on request"}</span><span><CarFront size={14}/>{vehicle.transmission||"Transmission on request"}</span></div><div className="market-car-bottom"><strong>{money(vehicle.priceKes)}</strong><a href={enquiry} target={whatsapp?"_blank":undefined} rel={whatsapp?"noreferrer":undefined} aria-label={"Enquire about "+vehicle.make+" "+vehicle.model}><ArrowUpRight size={19}/></a></div></div>
 </article>;
}

export default function Home(){
 const [vehicles,setVehicles]=useState<Vehicle[]>([]);
 const [loading,setLoading]=useState(true);
 const [loadError,setLoadError]=useState(false);
 const [settings,setSettings]=useState<Settings>({});
 const [query,setQuery]=useState("");
 const [filters,setFilters]=useState<Filters>(initialFilters);
 const [filtersOpen,setFiltersOpen]=useState(false);
 const [sort,setSort]=useState("newest");
 const [promo,setPromo]=useState(0);

 useEffect(()=>{
  let alive=true;
  fetch("/api/public/vehicles",{cache:"no-store"}).then(async r=>{if(!r.ok)throw new Error("Inventory unavailable");return r.json();}).then((items:Vehicle[])=>{if(alive)setVehicles(Array.isArray(items)?items:[]);}).catch(()=>{if(alive)setLoadError(true);}).finally(()=>{if(alive)setLoading(false);});
  fetch("/api/public/settings",{cache:"no-store"}).then(r=>r.ok?r.json():null).then(v=>{if(alive&&v)setSettings(v);}).catch(()=>{});
  return ()=>{alive=false;};
 },[]);

 const makes=useMemo(()=>Array.from(new Set(vehicles.map(v=>v.make).filter(Boolean))).sort(),[vehicles]);
 const bodies=useMemo(()=>Array.from(new Set(vehicles.map(v=>v.bodyType||"").filter(Boolean))).sort(),[vehicles]);
 const fuels=useMemo(()=>Array.from(new Set(vehicles.map(v=>v.fuel||"").filter(Boolean))).sort(),[vehicles]);
 const transmissions=useMemo(()=>Array.from(new Set(vehicles.map(v=>v.transmission||"").filter(Boolean))).sort(),[vehicles]);
 const filtered=useMemo(()=>{
  const q=query.trim().toLowerCase();
  const found=vehicles.filter(v=>{
   const text=[v.make,v.model,v.year,v.bodyType,v.fuel,v.transmission,v.exteriorColour].join(" ").toLowerCase();
   return (!q||text.includes(q))&&(!filters.make||v.make===filters.make)&&(!filters.body||v.bodyType===filters.body)&&(!filters.fuel||v.fuel===filters.fuel)&&(!filters.transmission||v.transmission===filters.transmission)&&(!filters.yearFrom||v.year>=Number(filters.yearFrom))&&(!filters.yearTo||v.year<=Number(filters.yearTo))&&(!filters.priceFrom||Number(v.priceKes)>=Number(filters.priceFrom))&&(!filters.priceTo||Number(v.priceKes)<=Number(filters.priceTo))&&(!filters.mileageTo||(v.mileageKm!==null&&Number(v.mileageKm)<=Number(filters.mileageTo)));
  });
  return found.sort((a,b)=>sort==="price-low"?Number(a.priceKes)-Number(b.priceKes):sort==="price-high"?Number(b.priceKes)-Number(a.priceKes):new Date(b.createdAt||0).getTime()-new Date(a.createdAt||0).getTime());
 },[vehicles,query,filters,sort]);
 const phone=(settings.phoneNumber||"").trim();
 const whatsapp=digits(settings.whatsappNumber||"");
 const email=(settings.contactEmail||"").trim();
 const wa=whatsapp?"https://wa.me/"+whatsapp+"?text="+encodeURIComponent("Hello Peachcar, I would like to enquire about a vehicle."):"#";
 const featured=vehicles[0];
 const update=(key:keyof Filters,value:string)=>setFilters(current=>({...current,[key]:value}));
 const activeCount=Object.values(filters).filter(Boolean).length;
 const clearFilters=()=>{setFilters(initialFilters);setQuery("");};

 return <>
  <Header phone={phone} whatsapp={whatsapp}/>
  <main className="market-main">
   <section className="market-hero"><div className="market-shell market-hero-grid">
    <div className="market-hero-feature"><div className="market-hero-copy"><span className="market-eyebrow"><Sparkles size={13}/> A BETTER WAY TO FIND YOUR NEXT CAR</span><h1>Good cars.<br/><em>Good decisions.</em></h1><p>Discover carefully presented vehicles, clear KES pricing and a team ready to help you make your next move.</p><a className="market-pink-btn" href="#inventory">Explore cars <ArrowUpRight size={16}/></a><div className="market-proof-row"><span><CarFront/> Live dealership inventory</span><span><ShieldCheck/> Guided buying experience</span></div></div><div className="market-hero-visual">{featured?.images?.[0]?.url?<img src={featured.images[0].url} alt={featured.make+" "+featured.model}/>:<div className="market-car-silhouette"><CarFront size={160} strokeWidth={0.7}/></div>}<span className="market-visual-caption">{featured?featured.make+" "+featured.model:"FIND YOUR NEXT DRIVE"} <b>{featured?money(featured.priceKes):"PEACHCAR"}</b></span></div><div className="market-hero-count"><b>01</b><span>/</span> 03</div></div>
    <div className="market-hero-side"><div className="market-side-promo"><span className="market-eyebrow">FOR EVERY KIND OF JOURNEY</span><h2>Find a car that<br/><em>feels like yours.</em></h2><p>Browse our published collection and enquire directly with our team.</p><a href="#inventory">Browse inventory <ArrowRight size={15}/></a><div className="market-promo-art"><CarFront size={112} strokeWidth={0.8}/></div></div><div className="market-side-help"><div><span>Need a hand?</span><strong>We are one message away.</strong></div>{whatsapp?<a href={wa} target="_blank" rel="noreferrer"><MessageCircle size={17}/></a>:<a href="#contact"><ArrowUpRight size={17}/></a>}</div></div>
   </div></section>

   <section className="market-search-wrap" id="inventory"><div className="market-shell"><div className="market-search-panel"><div className="market-search-heading"><div><span className="market-eyebrow">YOUR NEXT CAR STARTS HERE</span><h2>Find your dream car</h2></div><span className="market-live-indicator"><i/> Live inventory</span></div><div className="market-search-main"><Search size={20}/><input value={query} onChange={e=>setQuery(e.target.value)} placeholder="Search by make, model, year, fuel or colour"/><button onClick={()=>setFiltersOpen(!filtersOpen)}><SlidersHorizontal size={17}/> Filters {activeCount>0&&<b>{activeCount}</b>}</button></div>
    <div className="market-quick-filters"><label><span>Make</span><select value={filters.make} onChange={e=>update("make",e.target.value)}><option value="">All makes</option>{makes.map(x=><option key={x}>{x}</option>)}</select></label><label><span>Body type</span><select value={filters.body} onChange={e=>update("body",e.target.value)}><option value="">All body types</option>{bodies.map(x=><option key={x}>{x}</option>)}</select></label><label><span>Fuel</span><select value={filters.fuel} onChange={e=>update("fuel",e.target.value)}><option value="">All fuel types</option>{fuels.map(x=><option key={x}>{x}</option>)}</select></label><label><span>Transmission</span><select value={filters.transmission} onChange={e=>update("transmission",e.target.value)}><option value="">Any transmission</option>{transmissions.map(x=><option key={x}>{x}</option>)}</select></label><button className="market-filter-toggle" onClick={()=>setFiltersOpen(!filtersOpen)}>{filtersOpen?"Hide":"More"} filters <ChevronDown size={15}/></button></div>
    {filtersOpen&&<div className="market-advanced-filters"><label>Year from<input type="number" min="1950" max="2035" placeholder="e.g. 2015" value={filters.yearFrom} onChange={e=>update("yearFrom",e.target.value)}/></label><label>Year to<input type="number" min="1950" max="2035" placeholder="e.g. 2024" value={filters.yearTo} onChange={e=>update("yearTo",e.target.value)}/></label><label>Min price (KES)<input type="number" min="0" placeholder="No minimum" value={filters.priceFrom} onChange={e=>update("priceFrom",e.target.value)}/></label><label>Max price (KES)<input type="number" min="0" placeholder="No maximum" value={filters.priceTo} onChange={e=>update("priceTo",e.target.value)}/></label><label>Max mileage (km)<input type="number" min="0" placeholder="Any mileage" value={filters.mileageTo} onChange={e=>update("mileageTo",e.target.value)}/></label><button onClick={clearFilters}>Clear all filters</button></div>}
   </div></div></section>

   <section className="market-inventory-section"><div className="market-shell"><div className="market-inventory-top"><div className="market-inventory-tabs"><button className="active">All cars <b>{vehicles.length}</b></button><span>Published listings</span></div><label className="market-sort">Sort by <select value={sort} onChange={e=>setSort(e.target.value)}><option value="newest">Newest first</option><option value="price-low">Price: low to high</option><option value="price-high">Price: high to low</option></select></label></div><div className="market-results-heading"><div><span className="market-eyebrow">THE PEACHCAR COLLECTION</span><h2>Cars worth <em>looking at.</em></h2><p>{loading?"Loading available vehicles…":loadError?"We could not load the inventory right now.":filtered.length+" vehicle"+(filtered.length===1?"":"s")+" matching your search"}</p></div><a href="#contact">Need help choosing? <ArrowUpRight size={15}/></a></div>
    {loading?<div className="market-empty"><div className="market-loader"/><p>Loading live inventory from Peachcar…</p></div>:loadError?<div className="market-empty"><CircleHelp/><h3>Inventory is temporarily unavailable</h3><p>Please refresh in a moment or contact our team.</p></div>:filtered.length===0?<div className="market-empty"><Search/><h3>No matching cars found</h3><p>Try changing your search or clearing some filters.</p><button onClick={clearFilters}>Clear search and filters</button></div>:<div className="market-car-grid">{filtered.map(v=><VehicleCard key={v.id} vehicle={v} whatsapp={whatsapp}/>)}</div>}
    {filtered.length>0&&<div className="market-view-all"><span>Showing {filtered.length} of {vehicles.length} published vehicles</span><a href="#contact">Ask us to find a specific car <ArrowRight size={15}/></a></div>}
   </div></section>

   <section className="market-trust-band" id="services"><div className="market-shell"><div className="market-trust-intro"><span className="market-eyebrow">THE PEACHCAR DIFFERENCE</span><h2>More clarity.<br/><em>Less guesswork.</em></h2><p>Buying a car is a big decision. We make it easier to understand your options and take the next step with confidence.</p></div><div className="market-trust-cards"><article><span><ShieldCheck/></span><h3>Confidence first</h3><p>Clear vehicle information and a guided process from enquiry to viewing.</p></article><article><span><Wallet/></span><h3>Transparent pricing</h3><p>Vehicle prices displayed in Kenyan shillings, with no mystery figures.</p></article><article><span><Users/></span><h3>Real human support</h3><p>Speak with the Peachcar team when you need details or help arranging a viewing.</p></article></div></div></section>

   <section className="market-process-section" id="buyers"><div className="market-shell"><div className="market-section-title"><span className="market-eyebrow">BUYING WITH PEACHCAR</span><h2>From browsing to <em>your driveway.</em></h2><p>A clear path to your next set of keys.</p></div><div className="market-process-grid"><article><b>01</b><span><Search/></span><h3>Discover</h3><p>Browse available cars and narrow down the options that fit your needs.</p></article><article><b>02</b><span><CircleHelp/></span><h3>Ask and inspect</h3><p>Ask questions, request more information and arrange a viewing.</p></article><article><b>03</b><span><CheckCircle2/></span><h3>Make your decision</h3><p>Review the details with our team and agree on the next steps.</p></article><article><b>04</b><span><CarFront/></span><h3>Drive away</h3><p>Complete the process and get ready for the roads ahead.</p></article></div></div></section>

   <section className="market-sell-section" id="sellers"><div className="market-shell market-sell-card"><div className="market-sell-art"><CarFront size={170} strokeWidth={0.65}/><span>YOUR CAR'S NEXT CHAPTER</span></div><div><span className="market-eyebrow">SELL WITH PEACHCAR</span><h2>Ready to move on<br/>from your <em>current car?</em></h2><p>Tell us what you are selling. Our team can guide you through the next steps and help you understand the process.</p>{whatsapp?<a className="market-pink-btn" href={wa} target="_blank" rel="noreferrer">Talk to our team <ArrowUpRight size={16}/></a>:<a className="market-pink-btn" href="#contact">Contact our team <ArrowUpRight size={16}/></a>}</div></div></section>

   <section className="market-faq-section"><div className="market-shell market-faq-layout"><div><span className="market-eyebrow">GOOD TO KNOW</span><h2>Questions before<br/><em>the keys?</em></h2><p>Here are a few helpful notes about getting started with Peachcar.</p>{phone&&<a className="market-faq-call" href={"tel:"+phone.replace(/[^\\d+]/g,"")}><Phone size={16}/> Call our team</a>}</div><div className="market-faq-list"><details><summary>How do I enquire about a vehicle? <ChevronDown/></summary><p>Open a vehicle listing and use the enquiry link, or contact Peachcar using the details in the contact section below.</p></details><details><summary>Can I arrange to view a car? <ChevronDown/></summary><p>Yes. Contact the team with the vehicle you are interested in so viewing arrangements can be discussed.</p></details><details><summary>Are the prices shown in Kenyan shillings? <ChevronDown/></summary><p>Yes. Vehicle prices on Peachcar are displayed in KES.</p></details><details><summary>Can I sell my car through Peachcar? <ChevronDown/></summary><p>Contact the Peachcar team with your vehicle details to discuss the selling process.</p></details><details><summary>What if I cannot find the car I want? <ChevronDown/></summary><p>Send the team your preferred make, model and budget. They can advise you on the next step.</p></details></div></div></section>

   {(phone||whatsapp||email)&&<section className="market-contact-section" id="contact"><div className="market-shell"><div><span className="market-eyebrow">HERE WHEN YOU NEED US</span><h2>Let's talk <em>cars.</em></h2><p>Reach the Peachcar team using your preferred contact option.</p></div><div className="market-contact-options">{phone&&<a href={"tel:"+phone.replace(/[^\\d+]/g,"")}><Phone/><span><small>CALL US</small><strong>{phone}</strong></span><ArrowUpRight/></a>}{whatsapp&&<a href={wa} target="_blank" rel="noreferrer"><MessageCircle/><span><small>WHATSAPP</small><strong>Chat with our team</strong></span><ArrowUpRight/></a>}{email&&<a href={"mailto:"+email}><Mail/><span><small>EMAIL US</small><strong>{email}</strong></span><ArrowUpRight/></a>}</div></div></section>}
  </main>
  <footer className="market-footer"><div className="market-shell market-footer-main"><div><Brand/><p>A thoughtful way to find<br/>your next car in Kenya.</p></div><div><b>Explore</b><a href="#inventory">Find a car</a><a href="#buyers">Buying with Peachcar</a><a href="#sellers">Selling with Peachcar</a></div><div><b>Peachcar</b><a href="#services">Why Peachcar</a><a href="#buyers">How it works</a><a href="/admin">Admin sign in</a></div><div><b>Get in touch</b>{phone&&<a href={"tel:"+phone.replace(/[^\\d+]/g,"")}>{phone}</a>}{whatsapp&&<a href={wa} target="_blank" rel="noreferrer">WhatsApp ↗</a>}{email&&<a href={"mailto:"+email}>{email}</a>}</div></div><div className="market-shell market-footer-bottom"><span>© {new Date().getFullYear()} Peachcar. All rights reserved.</span><span>Made for the roads ahead. · Kenya</span></div></footer>
 </>;
}
