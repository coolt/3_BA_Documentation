% zhawinesba document class based on book
% created for a bachelor thesis at zhaw/ines
% -----------------------------------------------------------------
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{zhawinesba}[2015/02/26 ZHAW InES Standard Report]
\LoadClass[a4paper, 12pt, twoside, parskip=full]{book}
%parskip=half

\usepackage[ngerman]{babel}
\usepackage[utf8]{inputenc}
\usepackage{geometry}
\usepackage{graphicx}
\setkeys{Gin}{keepaspectratio=true} % default graphicx width to pagewidth
\usepackage{helvet}
%\usepackage{fancyhdr}
\usepackage{url}
\usepackage{tocloft} % for cftaddtitleline
\usepackage{todonotes}

% Page
% ====

\geometry{a4paper, top=30mm, inner=30mm, outer=25mm, bottom=30mm,headsep=10mm,footskip=10mm}

% lowercase heading for the table of contents
%\tableofcontents{\fancyhead[LE]{\slshape\contentsname}}
%\listoffigures{\fancyhead[LE]{\slshape\listfigurename}}
%\listoftables{\fancyhead[LE]{\slshape\listtablename}}

% do not uppercase the headers
\renewcommand{\chaptermark}[1]{\markboth{#1}{}}
\renewcommand{\sectionmark}[1]{\markright{#1}}

% do not uppercase the toc headers
\renewcommand{\cftmarktoc}{\@mkboth{\contentsname}{\contentsname}}

% do not bold the page numbers in the toc
\renewcommand{\cftchappagefont}{\normalfont}

% Bibliography
% ============
\usepackage[sectionbib]{natbib} % bibl will be a section and not a chapter
\bibliographystyle{abbrvnat}
\setcitestyle{authoryear,open={(},close={)}}

% Titles tweaking
% ===============
% No "chapter" label in chapter titles
\def\@chapapp{}

% numbering subsubsection with letters
% inspiré de /usr/share/texmf/tex/latex/base/book.cls
\renewcommand\thesubsubsection{\@alph\c@subsubsection.}
% in toc moins de retrait pour subsubsection...
%\makeatletter
\renewcommand*\l@subsubsection{\@dottedtocline{3}{7.0em}{1.5em}}
%\makeatother

\setcounter{secnumdepth}{4}
\setcounter{tocdepth}{4}

% Paragraphs and text
% ===================

\setlength{\parskip}{\baselineskip}
\setlength{\parindent}{0pt}
% add a command for a having an indent in a specific paragraph
\newcommand{\forceindent}{\leavevmode{\parindent=1em\indent}}

\renewcommand{\url}{\begingroup \small\Url}

% Tables and figures
% ==================

% one empty line before "here" figures (it's for all floats)
\addtolength{\intextsep}{\baselineskip}

% left aligned captions figures
\usepackage[justification=raggedright,singlelinecheck=false]{caption}

% a try to make the list of tables a section instead of a chapter
%\renewcommand\listoftables{%
%    \section{\contentsname}%
%    \@startlot{lot}%
%}


% Lists
% =====
% dashes instead of bullets for the lists
\usepackage{enumitem}
% no parskip between list items
\setlist[itemize]{parsep=0pt}
\setlist[enumerate]{parsep=0pt}

\renewcommand{\labelitemi}{$-$}
\renewcommand{\labelitemii}{$-$}


% Typography
% ==========
%\let\oldpercent=\%
%\renewcommand{\%}{\ifmmode\oldpercent\else\thinspace\oldpercent\fi}

% Title page
% ==========

% Redefinition of the \maketitle command to create the ZHAW
% title page, added an email parameter (\email)
% -----------------------------------------------------------------
\newcommand{\Email}{info.ines@zhaw.ch}
\newcommand{\email}[1]{\renewcommand{\Email}{#1}}
\renewcommand{\and}{\\}  % separate the authors with a line break

\newcommand{\subtitle}[1]{\def\@subtitle{#1}}
\newcommand{\tutor}[1]{\def\@tutor{#1}}
\newcommand{\cotutor}[1]{\def\@cotutor{#1}}

\renewcommand{\maketitle}{

\thispagestyle{empty}

% ignore header and footer space for the title page
%\geometry{a4paper, top=30mm, inner=30mm, outer=25mm, bottom=30mm,headsep=10mm,footskip=10mm}

\newgeometry{left=2cm,right=2cm, top=1cm, bottom=2cm}
%\vspace*{-2cm}
%enlargethispage{1cm}

% Place the logo on top left
\begin{flushleft}
    {\includegraphics[height=30mm]{images/logos/de-zhaw-ines-rgb.png}}
\end{flushleft}

\vskip 2em

% Title of the report
\begin{flushleft}
    %{\huge \textbf\sf{\@title}}
    \ifdefined\@subtitle
    {\normalfont\sffamily\Large{\@subtitle}}\\ % TODO: if we want to use it, we have to first define it as a variable
    \vspace*{0.4cm}
    \fi
    {\setlength{\baselineskip}{2em} \normalfont\sffamily\Huge{\@title}}
    %\rule{\textwidth}{0.8mm}
\end{flushleft}

\vskip 2em

% one cannot directly use newcommand to redefine an includegraphics path
% http://tex.stackexchange.com/questions/120969/passing-image-path-to-includegraphics-using-a-macro
\@ifundefined{titleImagePath}{\newcommand{\titleImagePath}{images/logos/de-zhaw-ines-rgb.png}}{}
\let\TitleImagePath\titleImagePath
\includegraphics[width=\textwidth]{\TitleImagePath}

\vfill

%%%%% ok

% School and Institute
\begin{flushleft}
    \normalfont\sffamily\Large
    {\textsc{Zürcher Hochschule \\ für Angewandte Wissenschaften}}\\
    \vspace*{0.4cm}
    %\Large
    \normalsize
    \textsc{Institute of Embedded Systems}
\end{flushleft}

\@ifundefined{@tutor}{\@tutor{}}{}
\@ifundefined{@cotutor}{\@cotutor{}}{}

% Authors and Date
%\vskip 5em
{
\normalfont\sffamily
\renewcommand{\arraystretch}{2} % add more space between the rows
\begin{tabular}{@{} p{3cm} l @{}}
Autoren & \parbox[t]{5cm}{\@author}\\
Hauptbetreuer & \parbox[t]{5cm}{\@tutor}\\
Nebenbetreuer & \parbox[t]{5cm}{\@cotutor}\\
& \\
Datum & \parbox[t]{5cm}{\@date}\\
\end{tabular}
}

% Second page with the contact information
% \newpage
% \begin{flushleft}
% \ifthenelse{\equal{\languagename}{\string ngerman}}{\textbf{Kontakt Adresse}}{\textbf{Contact Information}}\newline
% c/o Inst. of Embedded Systems (InES)\newline
% Zürcher Hochschule für Angewandte Wissenschaften \newline
% Technikumstrasse 22\newline
% CH-8401 Winterthur\newline
% \newline
% Tel.: +41 (0)58 934 75 25\newline
% Fax.: +41 (0)58 935 75 25\newline
% \newline
% E-Mail: \Email\newline
% Homepage: \url{http://www.ines.zhaw.ch}\newline
% \end{flushleft}
\restoregeometry
\pagebreak
}
\renewcommand*\l@subsubsection{\@dottedtocline{3}{7.0em}{1.5em}}
