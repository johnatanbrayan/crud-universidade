export interface IUser {
    id?: any;
    login?: string;
    nome?: string;
    dtNascimento?: Date;
    tipoUsuario?: number;
    sexo?: string;
    telCelular?: string;
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
    pai?: number;
    cpf?: string;
    cnpj?: string;
    cnh?: string;
    nomeFantasia?: string;
    score?: number;
    telFixo?: string;
    email?: string;
    activated?: boolean;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public nome?: string,
        public lastName?: string,
        public dtNascimento?: Date,
        public tipoUsuario?: number,
        public sexo?: string,
        public telCelular?: string,
        public logradouro?: string,
        public numero?: string,
        public complemento?: string,
        public bairro?: string,
        public cidade?: string,
        public uf?: string,
        public cep?: string,
        public coord?: string,
        public valor?: number,
        public token?: string,
        public pai?: number,
        public cpf?: string,
        public cnpj?: string,
        public cnh?: string,
        public nomeFantasia?: string,
        public score?: number,
        public telFixo?: string,
        public email?: string,
        public activated?: boolean,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.nome = nome ? nome : null;
        this.dtNascimento = dtNascimento ? dtNascimento : null;
        this.tipoUsuario = tipoUsuario ? tipoUsuario : null;
        this.sexo = sexo ? sexo : null;
        this.telCelular = telCelular ? telCelular : null;
        this.logradouro = logradouro ? logradouro : null;
        this.numero = numero ? numero : null;
        this.complemento = complemento ? complemento : null;
        this.bairro = bairro ? bairro : null;
        this.cidade = cidade ? cidade : null;
        this.uf = uf ? uf : null;
        this.cep = cep ? cep : null;
        this.coord = coord ? coord : null;
        this.valor = valor ? valor : null;
        this.token = token ? token : null;
        this.pai = pai ? pai : null;
        this.cpf = cpf ? cpf : null;
        this.cnpj = cnpj ? cnpj : null;
        this.cnh = cnh ? cnh : null;
        this.nomeFantasia = nomeFantasia ? nomeFantasia : null;
        this.score = score ? score : null;
        this.telFixo = telFixo ? telFixo : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
    }
}
