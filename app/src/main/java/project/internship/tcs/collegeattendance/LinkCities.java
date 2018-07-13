package project.internship.tcs.collegeattendance;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public interface LinkCities {
    String states="Andaman & Nicobar Islands,Andhra Pradesh,Arunachal Pradesh,Assam,Bihar,Chandigarh,Chattisgarh,Dadra & Nagar Haveli,Daman & Diu,Delhi,Goa,Gujarat,Haryana,Himachal Pradesh,Jammu & Kashmir,Jharkhand,Karnataka,Kerala,Lakshadweep,Madhya Pradesh,Maharashtra,Manipur,Meghalaya,Mizoram,Nagaland,Odisha,Poducherry,Punjab,Rajasthan,Sikkim,Tamil Nadu,Telangana,Tripura,Uttar Pradesh,Uttarakhand,West Bengal";
    String cities="Adilabad,Agra,Ahmed Nagar,Ahmedabad City,Aizawl,Ajmer,Akola,Aligarh,Allahabad,Alleppey,Almora,Alwar,Alwaye,Amalapuram,Ambala,Ambedkar Nagar,Amravati,Amreli,Amritsar,Anakapalle,Anand,Anantapur,Ananthnag,Anna Road H.O,Arakkonam,Asansol,Aska,Auraiya,Aurangabad,Aurangabad(Bihar),Azamgarh,Bagalkot,Bageshwar,Bagpat,Bahraich,Balaghat,Balangir,Balasore,Ballia,Balrampur,Banasanktha,Banda,Bandipur,Bankura,Banswara,Barabanki,Baramulla,Baran,Bardoli,Bareilly,Barmer,Barnala,Barpeta,Bastar,Basti,Bathinda,Beed,Begusarai,Belgaum,Bellary,Bengaluru East,Bengaluru South,Bengaluru West,Berhampur,Bhadrak,Bhagalpur,Bhandara,Bharatpur,Bharuch,Bhavnagar,Bhilwara,Bhimavaram,Bhiwani,Bhojpur,Bhopal,Bhubaneswar,Bidar,Bijapur,Bijnor,Bikaner,Bilaspur,Birbhum,Bishnupur,Bongaigaon,Budaun,Budgam,Bulandshahr,Buldhana,Bundi,Burdwan,Cachar,Calicut,Carnicobar,Chamba,Chamoli,Champawat,Champhai,Chandauli,Chandel,Chandigarh,Chandrapur,Changanacherry,Changlang,Channapatna,Chengalpattu,Chennai City Central,Chennai City North,Chennai City South,Chhatarpur,Chhindwara,Chikmagalur,Chikodi,Chitradurga,Chitrakoot,Chittoor,Chittorgarh,Churachandpur,Churu,Coimbatore,Contai,Cooch Behar,Cuddalore,Cuddapah,Cuttack City,Cuttack North,Cuttack South,Dadra & Nagar Haveli,Daman,Darbhanga,Darjiling,Darrang,Dausa,Dehra Gopipur,Dehradun,Delhi,Deoria,Dhalai,Dhanbad,Dharamsala,Dharmapuri,Dharwad,Dhemaji,Dhenkanal,Dholpur,Dhubri,Dhule,Dibang Valley,Dibrugarh,Diglipur,Dimapur,Dindigul,Diu,Doda,Dungarpur,Durg,East Champaran,East Garo Hills,East Kameng,East Khasi Hills,East Siang,East Sikkim,Eluru,Ernakulam,Erode,Etah,Etawah,Faizabad,Faridabad,Faridkot,Farrukhabad,Fatehgarh Sahib,Fatehpur,Fazilka,Ferrargunj,Firozabad,Firozpur,Gadag,Gadchiroli,Gandhinagar,Ganganagar,Gautam Buddha Nagar,Gaya,Ghaziabad,Ghazipur,Giridih,Goa,Goalpara,Gokak,Golaghat,Gonda,Gondal,Gondia,Gorakhpur,Gudivada,Gudur,Gulbarga,Guna,Guntur,Gurdaspur,Gurugram,Gwalior,Hailakandi,Hamirpur (HP),Hamirpur (UP),Hanamkonda,Hanumangarh,Hardoi,Haridwar,Hassan,Hathras,Haveri,Hazaribagh,Hindupur,Hingoli,Hissar,Hooghly,Hoshangabad,Hoshiarpur,Howrah,Hut Bay,Hyderabad City,Hyderabad South East,Idukki,Imphal East,Imphal West,Indore City,Indore Moffusil,Irinjalakuda,Jabalpur,Jaintia Hills,Jaipur,Jaisalmer,Jalandhar,Jalaun,Jalgaon,Jalna,Jalor,Jalpaiguri,Jammu,Jamnagar,Jaunpur,Jhalawar,Jhansi,Jhujhunu,Jodhpur,Jorhat,Junagadh,Jyotiba Phule Nagar,Kakinada,Kalahandi,Kamrup,Kanchipuram,Kannauj,Kanniyakumari,Kannur,Kanpur Dehat,Kanpur Nagar,Kapurthala,Karaikal,Karaikudi,Karauli,Karbi Anglong,Kargil,Karimganj,Karimnagar,Karnal,Karur,Karwar,Kasaragod,Kathua,Kaushambi,Keonjhar,Khammam (AP),Khammam (TL),Khandwa,Kheda,Kheri,Kiphire,Kodagu,Kohima,Kokrajhar,Kolar,Kolasib,Kolhapur,Kolkata,Koraput,Kota,Kottayam,Kovilpatti,Krishnagiri,Kulgam,Kumbakonam,Kupwara,Kurnool,Kurukshetra,Kurung Kumey,Kushinagar,Kutch,Lakhimpur,Lakshadweep,Lalitpur,Latur,Lawngtlai,Leh,Lohit,Longleng,Lower Subansiri,Lucknow,Ludhiana,Lunglei,Machilipatnam,Madhubani,Madurai,Mahabubnagar,Maharajganj,Mahesana,Mahoba,Mainpuri,Malda,Mammit,Mandi,Mandsaur,Mandya,Mangalore,Manjeri,Mansa,Marigaon,Mathura,Mau,Mavelikara,Mayabander,Mayiladuthurai,Mayurbhanj,Medak,Meerut,Meghalaya,Midnapore,Mirzapur,Moga,Mohali,Mokokchung,Mon,Monghyr,Moradabad,Morena,Muktsar,Mumbai,Murshidabad,Muzaffarnagar,Muzaffarpur,Mysore,Nadia,Nagaon,Nagapattinam,Nagaur,Nagpur,Nainital,Nalanda,Nalbari,Nalgonda,Namakkal,Nancorie,Nancowrie,Nanded,Nandurbar,Nandyal,Nanjangud,Narasaraopet,Nashik,Navsari,Nawadha,Nawanshahr,Nellore,Nilgiris,Nizamabad,North 24 Parganas,North Cachar Hills,North Dinajpur,North Sikkim,North Tripura,Osmanabad,Ottapalam,Palamau,Palghat,Pali,Panchmahals,Papum Pare,Parbhani,Parvathipuram,Patan,Pathanamthitta,Patiala,Patna,Pattukottai,Pauri Garhwal,Peddapalli,Peren,Phek,Phulbani,Pilibhit,Pithoragarh,Poducherry (PD),Poducherry (TN),Pollachi,Poonch,Porbandar,Port Blair,Prakasam,Pratapgarh,Proddatur,Pudukkottai,Pulwama,Pune,Puri,Purnea,Purulia,Puttur,Quilon,Raebareli,Raichur,Raigarh (CT),Raigarh (MH),Raipur,Rajahmundry,Rajauri,Rajkot,Rajsamand,Ramanathapuram,Rampur,Rampur Bushahr,Ranchi,Rangat,Ratlam,Ratnagiri,Reasi,Rewa,Ri Bhoi,Rohtak,Rohtas,Ropar,Rudraprayag,Rupnagar,Sabarkantha,Sagar,Saharanpur,Saharsa,Salem East,Salem West,Samastipur,Sambalpur,Sangareddy,Sangli,Sangrur,Sant Kabir Nagar,Sant Ravidas Nagar,Santhal Parganas,Saran,Satara,Sawai Madhopur,Secunderabad,Sehore,Senapati,Serchhip,Shahdol,Shahjahanpur,Shimla,Shimoga,Shrawasti,Sibsagar,Siddharthnagar,Sikar,Sindhudurg,Singhbhum,Sirohi,Sirsi,Sitamarhi,Sitapur,Sivaganga,Siwan,Solan,Solapur,Sonbhadra,Sonepat,Sonitpur,South 24 Parganas,South Dinajpur,South Garo Hills,South Sikkim,South Tripura,Srikakulam,Srinagar,Srirangam,Sultanpur,Sundargarh,Surat,Surendranagar,Suryapet,Tadepalligudem,Tambaram,Tamenglong,Tamluk,Tarn Taran,Tawang,Tehri Garhwal,Tenali,Thalassery,Thane,Thanjavur,Theni,Thoubal,Tinsukia,Tirap,Tiruchirapalli,Tirunelveli,Tirupati,Tirupattur,Tirupur,Tirur,Tiruvalla,Tiruvannamalai,Tonk,Trichur,Trivandrum North,Trivandrum South,Tuensang,Tumkur,Tuticorin,Udaipur,Udham Singh Nagar,Udhampur,Udupi,Ujjain,Ukhrul,Una,Unnao,Upper Siang,Upper Subansiri,Uttarkashi,Vadakara,Vadodara East,Vadodara West,Vaishali,Valsad,Varanasi,Vellore,Vidisha,Vijayawada,Virudhunagar,Visakhapatnam,Vizianagaram,Vriddhachalam,Wanaparthy,Warangal,Wardha,Washim,West Champaran,West Garo Hills,West Kameng,West Khasi Hills,West Siang,West Sikkim,West Tripura,Wokha,Yavatmal,Zunhebotto";
    String link="32,34,21,12,24,29,21,34,34,18,35,29,18,2,13,34,21,12,28,2,12,2,15,31,31,36,26,34,21,5,34,17,35,34,34,20,26,26,34,34,12,34,15,36,29,34,15,29,12,34,29,28,4,7,34,28,21,5,17,17,17,17,17,26,26,5,21,29,12,12,29,2,13,5,20,26,17,17,34,29,7,36,22,4,34,15,34,21,29,36,4,18,1,14,35,35,24,34,22,6,21,18,3,17,31,31,31,31,20,20,17,17,17,34,2,29,22,29,31,36,36,31,2,26,26,26,8,9,5,36,4,29,14,35,10,34,33,16,14,31,17,4,26,29,4,21,3,4,1,25,31,9,15,29,7,5,23,3,23,3,30,2,18,31,34,34,34,13,28,34,28,34,28,1,34,28,17,21,12,29,34,5,34,34,16,11,4,17,4,34,12,21,34,2,2,17,20,2,28,13,20,4,14,34,32,29,34,35,17,34,17,16,2,21,13,36,20,28,36,1,32,32,18,22,22,20,20,18,20,23,29,29,28,34,21,21,29,36,15,12,34,29,34,29,29,4,12,34,2,26,4,31,34,31,18,34,34,28,27,31,29,4,15,4,32,13,31,17,18,15,34,26,2,32,20,12,34,25,17,25,4,17,24,21,36,26,29,18,31,31,15,31,15,2,13,3,34,12,4,19,34,21,24,15,3,25,3,34,28,24,2,5,31,32,34,12,34,34,36,24,14,20,17,17,18,28,4,34,34,18,1,31,26,32,34,23,36,34,28,28,25,25,5,34,20,28,21,36,34,5,17,36,4,31,29,21,35,5,4,32,31,1,1,21,21,2,17,2,21,12,5,28,2,31,32,36,4,36,30,33,21,18,16,18,29,12,3,21,2,12,18,28,5,31,35,32,25,25,26,34,35,27,31,31,15,12,1,2,34,2,31,15,21,26,5,36,17,18,34,17,7,21,7,2,15,12,29,31,34,14,16,1,20,21,15,20,23,13,5,28,35,28,12,20,34,5,31,31,5,26,32,21,28,34,34,16,5,21,29,32,20,22,24,20,34,14,17,34,4,34,29,21,16,29,17,5,34,31,5,14,21,34,13,4,36,36,23,30,33,2,15,31,34,26,12,12,32,2,31,22,36,28,3,35,2,18,21,31,31,22,4,3,31,31,2,31,31,18,18,31,29,18,18,18,25,17,31,29,35,15,17,20,22,14,34,3,3,35,18,12,12,5,12,34,31,20,2,31,2,2,31,32,32,21,21,5,23,3,23,3,30,33,25,21,25";
    public static ArrayAdapter<String> addStates(Context context){
        String[]st=states.split(",");
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,st);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  stringArrayAdapter;
    }
    public static ArrayAdapter<String> addCities(Context context, int position){
        ArrayList<String>ci=new ArrayList<String>();
        String[]li=link.split(",");
        String[]cit=cities.split(",");
        for (int i=0;i<li.length;i++){
            if(li[i].equals(String.valueOf(position+1)))
                ci.add(cit[i]);
        }
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,ci);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  stringArrayAdapter;
    }
}
