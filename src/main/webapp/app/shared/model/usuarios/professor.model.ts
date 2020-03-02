export interface IProfessor {
    id?: any;
    login?: string;
    email?: string;
    nome?: string;
    idPaiResponsavel?: number;
    authorities?: any[];
    activated?: boolean;
    dtNascimento?: Date;
    sexo?: string;
    telCelular?: string;
    status?: boolean;
    logradouro?: string;
    numero?: string;
    complemento?: string;
    bairro?: string;
    cidade?: string;
    uf?: string;
    cep?: string;
    coord?: string;
    valor?: number;
    token?: string;
}

export class Professor implements IProfessor {
    constructor(
        public id?: any,
        public login?: string,
        public email?: string,
        public nome?: string,
        public idPaiResponsavel?: number,
        public authorities?: any[],
        public activated?: boolean,
        public dtNascimento?: Date,
        public sexo?: string,
        public telCelular?: string,
        public status?: boolean,
        public logradouro?: string,
        public numero?: string,
        public complemento?: string,
        public bairro?: string,
        public cidade?: string,
        public uf?: string,
        public cep?: string,
        public coord?: string,
        public valor?: number,
        public token?: string
    ) {}
}
